package org.lostfan.ktv.view.report;

import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.model.DailyRegisterModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.dto.DailyRegisterReport;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.NotNullValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.components.EntityPanel;
import org.lostfan.ktv.view.components.EntityPanelFactory;

public class DailyRegisterView extends FormView {

    private class ReportTableModel extends AbstractTableModel {

        private List<PaymentExt> paymentExts = new ArrayList<>();
        private String[] columnNames;

        public ReportTableModel() {
            this.columnNames = new String[]
                    {
                            ResourceBundles.getEntityBundle().getString("subscriber"),
                            ResourceBundles.getEntityBundle().getString("service"),
                            ResourceBundles.getEntityBundle().getString("servicePrice.price")
                    };
        }

        public ReportTableModel(List<PaymentExt> payments) {
            this();
            this.paymentExts = payments;
        }

        @Override
        public int getRowCount() {
            return paymentExts.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return this.paymentExts.get(rowIndex).getSubscriber() != null ? String.format("%s (%d)",
                            this.paymentExts.get(rowIndex).getSubscriber().getName(),
                            this.paymentExts.get(rowIndex).getSubscriber().getAccount()) :
                            this.paymentExts.get(rowIndex).getSubscriberAccount();
                case 1:
                    return this.paymentExts.get(rowIndex).getSubscriber() != null ?
                            this.paymentExts.get(rowIndex).getService().getName() :
                            null;
                case 2:
                    return this.paymentExts.get(rowIndex).getPrice();
            }

            return null;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return this.columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return Integer.class;
            }
            return String.class;
        }
    }

    private class FooterModel extends AbstractTableModel {

        BigDecimal allPrice;

        public FooterModel() {
        }

        public FooterModel(BigDecimal allPrice) {
            this.allPrice = allPrice;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return getGuiString("inTotal");
                case 2:
                    return allPrice;
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return Integer.class;
            }
            return String.class;
        }
    }

    private class ServiceFormField extends FormField<Integer> {

        private EntityPanel panel;

        public ServiceFormField(String fieldKey) {
            super(fieldKey);
            this.panel = EntityPanelFactory.createEntityPanel(EntityFieldTypes.Service);

            this.panel.setParentView(DailyRegisterView.this);
        }

        @Override
        public JComponent getInputComponent() {
            return panel;
        }

        @Override
        public Integer getValue() {
            return this.panel.getSelectedEntity() != null ? this.panel.getSelectedEntity().getId() : null;
        }

        @Override
        public void setValue(Integer value) {

        }
    }

    private NotNullValidator validator = new NotNullValidator();
    private JButton generateButton;
    private JButton cancelButton;
    private JButton excelButton;
    private JTable reportTable;
    private JTable footerTable;

    private DailyRegisterModel model;

    private ViewActionListener generateActionListener;
    private ViewActionListener excelActionListener;

    private DateFormField dateField;

    public DailyRegisterView(DailyRegisterModel model) {
        this.model = model;
        reportTable = new JTable(new ReportTableModel());
        this.reportTable.setPreferredScrollableViewportSize(new Dimension(500, 70));

        this.footerTable = new JTable(new FooterModel());

        setTitle(getEntityString(model.getEntityNameKey()));

        dateField = new DateFormField("renderedService.date");
        addFormField(dateField);

        this.generateButton = new JButton(getGuiString("buttons.generateReport"));
        this.generateButton.setEnabled(model.getDate() != null);
        this.generateButton.addActionListener(e -> {
            if (this.generateActionListener != null) {
                this.generateActionListener.actionPerformed(dateField.getValue());
            }
        });
        // Disable "Generate button" on empty date
        // This is kind of a validator
        dateField.addValueListener(newValue -> {
            this.generateButton.setEnabled(newValue != null);
        });

        this.excelButton = new JButton(getGuiString("buttons.generateExcelReport"));
        URL url = getClass().getClassLoader().getResource("images/excel.png");
        if(url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
            excelButton.setIcon(icon);
        }
        this.excelButton.addActionListener(e -> {
            if (excelActionListener != null) {
                excelActionListener.actionPerformed(dateField.getValue());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> hide());

        model.addObserver(arg -> updateData());
        updateData();
        buildLayout();
        show();
    }

    private void updateData() {
        DailyRegisterReport report = this.model.getReport();
        if (report == null) {
            this.excelButton.setVisible(false);
            this.reportTable.setModel(new ReportTableModel());
            this.footerTable.setModel(new FooterModel());
        } else {
            this.excelButton.setVisible(true);
            this.reportTable.setModel(new ReportTableModel(report.getPayments()));
            this.footerTable.setModel(new FooterModel(report.getOverallSum()));
        }
        revalidate();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);

        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.reportTable.getColumnModel().getColumn(0).setCellRenderer(renderer);

        JScrollPane tableScrollPane = new JScrollPane(this.reportTable);
        panel.add(BorderLayout.CENTER, tableScrollPane);
        footerTable.setRowSelectionAllowed(false);
        footerTable.setColumnSelectionAllowed(false);

        tableScrollPane = new JScrollPane(footerTable);
        footerTable.setTableHeader(null);
        footerTable.setPreferredScrollableViewportSize(new Dimension(500, 17));

        panel.add(BorderLayout.SOUTH, tableScrollPane);

        getContentPanel().add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(excelButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showExceptionWindow(String message) {
        int optionType = JOptionPane.OK_OPTION;
        int messageType = JOptionPane.WARNING_MESSAGE;
        Object[] selValues = {getGuiString("buttons.ok")};
        String attention = getGuiString("message.attention");
        JOptionPane.showOptionDialog(null,
                getGuiString(message), attention,
                optionType, messageType, null, selValues,
                selValues[0]);
    }

    public void setGenerateActionListener(ViewActionListener generateActionListener) {
        this.generateActionListener = generateActionListener;
    }

    public void setExcelActionListener(ViewActionListener excelActionListener) {
        this.excelActionListener = excelActionListener;
    }
}
