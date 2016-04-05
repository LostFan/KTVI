package org.lostfan.ktv.view.report;


import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.ConsolidatedRegisterPaymentModel;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.components.EntityPanel;
import org.lostfan.ktv.view.components.EntityPanelFactory;

public class ConsolidatedRegisterPaymentView extends FormView {


    private class ReportTableModel extends AbstractTableModel {

        private List<Payment> payments = new ArrayList<>();
        private LocalDate date;

        public ReportTableModel() {
        }

        public ReportTableModel(List<Payment> payments, LocalDate date) {
            this.payments = payments;
            this.date = date;
        }

        @Override
        public int getRowCount() {
            if (date == null) {
                return 0;
            }
            return date.lengthOfMonth();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return this.payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.SUBSCRIPTION_FEE.getId())
                            .filter(e -> e.getDate().getDayOfMonth() == rowIndex + 1)
                            .mapToInt(e -> e.getPrice()).sum();
                case 2:
                    return this.payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.CONNECTION.getId())
                            .filter(e -> e.getDate().getDayOfMonth() == rowIndex + 1)
                            .mapToInt(e -> e.getPrice()).sum();
                case 3:
                    return this.payments.stream().filter(e -> e.getServicePaymentId() != FixedServices.SUBSCRIPTION_FEE.getId())
                            .filter(e -> e.getServicePaymentId() != FixedServices.CONNECTION.getId())
                            .filter(e -> e.getDate().getDayOfMonth() == rowIndex + 1)
                            .mapToInt(e -> e.getPrice()).sum();
                case 4:
                    return this.payments.stream()
                            .filter(e -> e.getDate().getDayOfMonth() == rowIndex + 1)
                            .mapToInt(e -> e.getPrice()).sum();
            }

            return null;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundles.getGuiBundle().getString(
                            "number");
                case 1:
                    return ResourceBundles.getEntityBundle().getString(
                            "subscriptionFee");
                case 2:
                    return ResourceBundles.getEntityBundle().getString(
                            "connection");
                case 3:
                    return ResourceBundles.getGuiBundle().getString(
                            "additionalServices");
                case 4:
                    return ResourceBundles.getGuiBundle().getString(
                            "total");
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

    private class FooterModel extends AbstractTableModel {

        private List<Payment> payments = new ArrayList<>();

        public FooterModel() {
        }

        public FooterModel(List<Payment> payments) {
            this.payments = payments;
        }


        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return getGuiString("inTotal");
                case 1:
                    return this.payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.SUBSCRIPTION_FEE.getId())
                            .mapToInt(e -> e.getPrice()).sum();
                case 2:
                    return this.payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.CONNECTION.getId())
                            .mapToInt(e -> e.getPrice()).sum();
                case 3:
                    return this.payments.stream().filter(e -> e.getServicePaymentId() != FixedServices.SUBSCRIPTION_FEE.getId())
                            .filter(e -> e.getServicePaymentId() != FixedServices.CONNECTION.getId())
                            .mapToInt(e -> e.getPrice()).sum();
                case 4:
                    return this.payments.stream()
                            .mapToInt(e -> e.getPrice()).sum();
            }

            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return String.class;
            }
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

            this.panel.setParentView(ConsolidatedRegisterPaymentView.this);
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
    private JTable footerTable;

    private ConsolidatedRegisterPaymentModel model;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;

    private DateFormField dateField;
    private BooleanFormField isAdditionalField;
    private ServiceFormField serviceField;

    public ConsolidatedRegisterPaymentView(ConsolidatedRegisterPaymentModel model) {
        this.model = model;
        reportTable = new JTable(new ReportTableModel());
        this.reportTable.setPreferredScrollableViewportSize(new Dimension(500, 70));

        setTitle(getEntityString(model.getEntityNameKey()));

        dateField = new DateFormField("renderedService.date");
        addFormField(dateField);
        isAdditionalField = new BooleanFormField("service.additional");
//        addFormField(isAdditionalField);
        serviceField = new ServiceFormField("service");
//        addFormField(serviceField);

        this.addButton = new JButton(getGuiString("buttons.generateReport"));
        this.addButton.addActionListener(e -> {
            List<Payment> payments = model.getByMonth(dateField.getValue());
            List<Payment> newPaymentList = payments.stream().filter(e1 -> e1.getServicePaymentId() == FixedServices.SUBSCRIPTION_FEE.getId())
                    .filter(e1 -> e1.getDate().getDayOfMonth() == 5).collect(Collectors.toList());
            ReportTableModel reportTableModel = new ReportTableModel(
                    payments, dateField.getValue());
            reportTable.setModel(reportTableModel);
            if (payments.size() > 0) {
                footerTable.setModel(new FooterModel(payments));
            }
            reportTable.repaint();
            if (this.addActionListener != null) {

            }
        });
        this.excelButton = new JButton(getGuiString("buttons.generateExcelReport"));
        this.excelButton.addActionListener(e -> {
            if (dateField.getValue() == null) {
                dateField.setError("errors.empty");
                return;
            }
            dateField.clearError();
            String message = model.generateExcelReport(dateField.getValue());
            if (message != null) {
                exceptionWindow(message);
            }

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

        buildLayout();

        show();
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
        footerTable = new JTable(new FooterModel());
        footerTable.setRowSelectionAllowed(false);
        footerTable.setColumnSelectionAllowed(false);

        tableScrollPane = new JScrollPane(footerTable);
        footerTable.setTableHeader(null);
        footerTable.setPreferredScrollableViewportSize(new Dimension(500, 17));

        panel.add(BorderLayout.SOUTH, tableScrollPane);

        getContentPanel().add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(excelButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }


    private void exceptionWindow(String message) {
        int optionType = JOptionPane.OK_OPTION;
        int messageType = JOptionPane.WARNING_MESSAGE;
        Object[] selValues = {getGuiString("buttons.ok")};
        String attention = getGuiString("message.attention");
        JOptionPane.showOptionDialog(null,
                getGuiString(message), attention,
                optionType, messageType, null, selValues,
                selValues[0]);
    }
}
