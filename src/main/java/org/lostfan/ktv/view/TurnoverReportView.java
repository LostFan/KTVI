package org.lostfan.ktv.view;


import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.TurnoverReportModel;
import org.lostfan.ktv.model.dto.TurnoverSheetTableDTO;
import org.lostfan.ktv.utils.*;
import org.lostfan.ktv.view.components.EntityPanel;
import org.lostfan.ktv.view.components.EntityPanelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TurnoverReportView extends FormView {

    private class ModelObserver implements org.lostfan.ktv.utils.Observer {
        @Override
        public void update(Object args) {
            TurnoverReportView.this.progressBar.setValue((Integer) args);
//            TurnoverReportView.this.progressBar.revalidate();
//            TurnoverReportView.this.progressBar.repaint();
//            TurnoverReportView.this.revalidate();
        }
    }

    private class ReportTableModel extends AbstractTableModel {

        private List<TurnoverSheetTableDTO> turnoverSheetTableDTOs = new ArrayList<>();

        public ReportTableModel() {
        }

        public ReportTableModel(List<TurnoverSheetTableDTO> turnoverSheetTableDTOs) {
            this.turnoverSheetTableDTOs = turnoverSheetTableDTOs;
        }

        @Override
        public int getRowCount() {
            return turnoverSheetTableDTOs.size();
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: return this.turnoverSheetTableDTOs.get(rowIndex).getSubscriberAccount();
                case 1: return this.turnoverSheetTableDTOs.get(rowIndex).getBroughtForwardBalanceCredit();
                case 2: return this.turnoverSheetTableDTOs.get(rowIndex).getBroughtForwardBalanceDebit();
                case 3: return this.turnoverSheetTableDTOs.get(rowIndex).getTurnoverBalanceCredit();
                case 4: return this.turnoverSheetTableDTOs.get(rowIndex).getTurnoverBalanceDebit();
                case 5: return this.turnoverSheetTableDTOs.get(rowIndex).getCarriedForwardBalanceCredit();
                case 6: return this.turnoverSheetTableDTOs.get(rowIndex).getCarriedForwardBalanceDebit();
            }

            return null;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: return ResourceBundles.getEntityBundle().getString(
                        "subscriber");
                case 1: return ResourceBundles.getGuiBundle().getString(
                        "broughtForwardBalanceCredit");
                case 2: return ResourceBundles.getGuiBundle().getString(
                        "broughtForwardBalanceDebit");
                case 3: return ResourceBundles.getGuiBundle().getString(
                        "turnoverBalanceCredit");
                case 4: return ResourceBundles.getGuiBundle().getString(
                        "turnoverBalanceDebit");
                case 5: return ResourceBundles.getGuiBundle().getString(
                        "carriedForwardBalanceCredit");
                case 6: return ResourceBundles.getGuiBundle().getString(
                        "carriedForwardBalanceDebit");
            }

            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Integer.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    private class ServiceFormField extends FormField<Integer> {

        private EntityPanel panel;

        public ServiceFormField(String fieldKey) {
            super(fieldKey);
            this.panel = EntityPanelFactory.createEntityPanel(EntityFieldTypes.Service);

            this.panel.setParentView(TurnoverReportView.this);
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

    private JButton addButton;
    private JButton cancelButton;
    private JButton excelButton;
    private JTable reportTable;
    private JProgressBar progressBar;

    private TurnoverReportModel model;

    private ModelObserver modelObserver;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;

    private DateFormField dateField;
    private BooleanFormField isAdditionalField;
    private ServiceFormField serviceField;

    public TurnoverReportView(TurnoverReportModel model) {
        this(model, null);
    }

    public TurnoverReportView(TurnoverReportModel model, Entity entity) {
        this.model = model;
        reportTable = new JTable(new ReportTableModel());
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        this.reportTable.setPreferredScrollableViewportSize(new Dimension(500, 70));

        setTitle(getEntityString(model.getEntityNameKey()));

        dateField = new DateFormField("renderedService.date");
        addFormField(dateField);
        isAdditionalField = new BooleanFormField("service.additional");
        addFormField(isAdditionalField);
        serviceField = new ServiceFormField("service");
        addFormField(serviceField);

        this.addButton = new JButton(getGuiString("buttons.generateReport"));
        this.addButton.addActionListener(e -> {
            ReportTableModel reportTableModel;
            if(isAdditionalField.getValue()) {
                reportTableModel = new ReportTableModel(
                        model.getTurnoverSheetDataByAdditionalServices(dateField.getValue()));
            } else {
                reportTableModel = new ReportTableModel(
                        model.getTurnoverSheetData(dateField.getValue(), serviceField.getValue()));
            }
            reportTable.setModel(reportTableModel);
            reportTable.repaint();
            if (this.addActionListener != null) {

            }
        });
        this.excelButton = new JButton(getGuiString("buttons.generateExcelReport"));
        this.excelButton.addActionListener(e -> {
            if(dateField.getValue() == null) {
                dateField.setError("errors.empty");
                return;
            }
            dateField.clearError();
            if(!isAdditionalField.getValue() && serviceField.getValue() == null) {
                serviceField.setError("errors.empty");
                return;
            }
            serviceField.clearError();
            new Thread(() -> {
                String message = model.generateExcelReport(isAdditionalField.getValue(),
                        serviceField.getValue(), dateField.getValue());
                if (message != null) {
                    exceptionWindow(message);
                }
            }).start();

        });



        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            if (this.cancelActionListener != null) {
                this.cancelActionListener.actionPerformed(null);
            }
            hide();
        });

        for (EntityField entityField : model.getFields()) {
            if (!entityField.isEditable()) {
                continue;
            }

        }



        this.isAdditionalField.addValueListener(newValue -> {
            if ((isAdditionalField.getValue())) {
                serviceField.setVisible(false);
            } else {
                serviceField.setVisible(true);
            }
        });

        this.modelObserver = new ModelObserver();

        model.addObserver(this.modelObserver);

        buildLayout();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.reportTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
//        addStringActionTableCellEditorToColumns();
        JScrollPane tableScrollPane = new JScrollPane(this.reportTable);

        getContentPanel().add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(excelButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(progressBar);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }


    private void exceptionWindow(String message) {
        int optionType = JOptionPane.OK_OPTION;
        int messageType = JOptionPane.WARNING_MESSAGE;
        Object[] selValues = { getGuiString("buttons.ok")};
        String attention = getGuiString("message.attention");
        JOptionPane.showOptionDialog(null,
                getGuiString(message), attention,
                optionType, messageType, null, selValues,
                selValues[0]);
    }
}
