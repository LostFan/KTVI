package org.lostfan.ktv.view.entity;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.RenderedServiceExt;
import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.view.FormView;

public class SubscriberInformationView extends FormView {

    private class RenderedServiceAndPayment {

        public RenderedServiceAndPayment(RenderedServiceExt renderedService) {
            this.date = renderedService.getDate();
            this.service = renderedService.getService();
            this.price = renderedService.getPrice();
        }

        public RenderedServiceAndPayment(PaymentExt payment) {
            this.date = payment.getDate();
            this.isCredit = true;
            this.service = payment.getService();
            this.price = payment.getPrice();
        }

        private LocalDate date;

        private boolean isCredit;

        private Service service;

        private Integer price;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public boolean isCredit() {
            return isCredit;
        }

        public void setCredit(boolean credit) {
            isCredit = credit;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

    }

    private class SubscriptionFeeRenderedServicesTableModel extends AbstractTableModel {

        private List<LocalDate> datesList = new ArrayList<>();
        private List<RenderedServiceAndPayment> renderedServiceAndPayments;

        public SubscriptionFeeRenderedServicesTableModel(List<RenderedServiceAndPayment> renderedServiceAndPayments) {
            this.renderedServiceAndPayments = renderedServiceAndPayments;
            LocalDate date = LocalDate.now().withDayOfMonth(1);
            if (this.renderedServiceAndPayments.size() > 0) {
                date = this.renderedServiceAndPayments.stream()
                        .min((o1, o2) -> o1.getDate().compareTo(o2.getDate())).get().getDate();
            }
            while (date.isBefore(LocalDate.now())) {
                datesList.add(date.withDayOfMonth(1));
                date = date.plusMonths(1);
            }
        }

        @Override
        public int getRowCount() {
            return datesList.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundles.getEntityBundle().getString(
                            "renderedService.date");
                case 1:
                    return ResourceBundles.getGuiBundle().getString(
                            "broughtForwardBalance");
                case 2:
                    return ResourceBundles.getGuiBundle().getString(
                            "chargeable");

            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return Integer.class;
                case 2:
                    return Integer.class;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            switch (columnIndex) {
                case 0:
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM.yyyy");
                    return dateTimeFormatter.format(datesList.get(rowIndex));
                case 1:
                    return this.renderedServiceAndPayments.stream()
                            .filter(o -> o.getDate().isBefore(datesList.get(rowIndex)))
                            .mapToInt(o -> o.isCredit() ? -1 * o.getPrice() : o.getPrice()).sum();
                case 2:
                    return this.renderedServiceAndPayments.stream()
                            .filter(o -> o.getDate().withDayOfMonth(1).isEqual(datesList.get(rowIndex)))
                            .filter(o -> !o.isCredit())
                            .mapToInt(o -> o.getPrice()).sum();
            }
            return null;
        }
    }

    private class PaymentsTableModel extends AbstractTableModel {

        private List<PaymentExt> payments;

        public PaymentsTableModel(List<PaymentExt> payments) {
            this.payments = payments.stream()
                    .sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
                    .collect(Collectors.toList());
        }

        @Override
        public int getRowCount() {
            return payments.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundles.getEntityBundle().getString(
                            "renderedService.date");
                case 1:
                    return ResourceBundles.getGuiBundle().getString(
                            "paid");
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return Integer.class;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            switch (columnIndex) {
                case 0:
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    return dateTimeFormatter.format(payments.get(rowIndex).getDate());
                case 1:
                    return payments.get(rowIndex).getPrice();
            }
            return null;

        }
    }

    private class TariffsTableModel extends AbstractTableModel {

        private List<SubscriberTariff> subscriberTariffs;

        public TariffsTableModel(List<SubscriberTariff> subscriberTariffs) {
            this.subscriberTariffs = subscriberTariffs.stream()
                    .sorted((o1, o2) -> o1.getConnectTariff().compareTo(o2.getConnectTariff()))
                    .collect(Collectors.toList());
        }

        @Override
        public int getRowCount() {
            return subscriberTariffs.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundles.getEntityBundle().getString(
                            "connection");
                case 1:
                    return ResourceBundles.getEntityBundle().getString(
                            "tariff");
                case 2:
                    return ResourceBundles.getEntityBundle().getString(
                            "disconnection");
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            switch (columnIndex) {
                case 0:
                    return dateTimeFormatter.format(subscriberTariffs.get(rowIndex).getConnectTariff());
                case 1:
                    return subscriberTariffs.get(rowIndex).getTariffId();
                case 2:
                    return subscriberTariffs.get(rowIndex).getDisconnectTariff() != null ?
                            dateTimeFormatter.format(subscriberTariffs.get(rowIndex).getDisconnectTariff()) :
                            null;
            }
            return null;

        }
    }

    private class SessionsTableModel extends AbstractTableModel {

        private List<SubscriberSession> subscriberSessions;

        public SessionsTableModel(List<SubscriberSession> subscriberSessions) {
            this.subscriberSessions = subscriberSessions.stream()
                    .sorted((o1, o2) -> o1.getConnectionDate().compareTo(o2.getConnectionDate()))
                    .collect(Collectors.toList());
        }

        @Override
        public int getRowCount() {
            return subscriberSessions.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundles.getEntityBundle().getString(
                            "connection");
                case 1:
                    return ResourceBundles.getEntityBundle().getString(
                            "disconnection");
                case 2:
                    return ResourceBundles.getEntityBundle().getString(
                            "disconnectionReason");
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            switch (columnIndex) {
                case 0:
                    return dateTimeFormatter.format(subscriberSessions.get(rowIndex).getConnectionDate());
                case 1:
                    return subscriberSessions.get(rowIndex).getDisconnectionDate() != null ?
                            dateTimeFormatter.format(subscriberSessions.get(rowIndex).getDisconnectionDate()) :
                            null;
                case 2:
                    return subscriberSessions.get(rowIndex).getDisconnectionReasonId();
            }
            return null;

        }
    }

    private class OtherRenderedServicesTableModel extends AbstractTableModel {

        private List<RenderedServiceAndPayment> renderedServiceAndPayments;

        public OtherRenderedServicesTableModel(List<RenderedServiceAndPayment> renderedServiceAndPayments) {
            this.renderedServiceAndPayments = renderedServiceAndPayments.stream()
                    .sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
                    .collect(Collectors.toList());
        }

        @Override
        public int getRowCount() {
            return renderedServiceAndPayments.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundles.getEntityBundle().getString(
                            "payment.payDate");
                case 1:
                    return ResourceBundles.getEntityBundle().getString(
                            "payment.price");
                case 2:
                    return ResourceBundles.getEntityBundle().getString(
                            "service");
                case 3:
                    return ResourceBundles.getEntityBundle().getString(
                            "renderedService.date");
                case 4:
                    return ResourceBundles.getEntityBundle().getString(
                            "renderedService.price");
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return Integer.class;
                case 2:
                    return String.class;
                case 3:
                    return String.class;
                case 4:
                    return Integer.class;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            switch (columnIndex) {
                case 0:
                    return renderedServiceAndPayments.get(rowIndex).isCredit() ?
                            dateTimeFormatter.format(renderedServiceAndPayments.get(rowIndex).getDate()) :
                            null;
                case 1:
                    return renderedServiceAndPayments.get(rowIndex).isCredit() ?
                            renderedServiceAndPayments.get(rowIndex).getPrice() :
                            null;
                case 2:
                    return renderedServiceAndPayments.get(rowIndex).getService().getName();
                case 3:
                    return !renderedServiceAndPayments.get(rowIndex).isCredit() ?
                            dateTimeFormatter.format(renderedServiceAndPayments.get(rowIndex).getDate()) :
                            null;
                case 4:
                    return !renderedServiceAndPayments.get(rowIndex).isCredit() ?
                            renderedServiceAndPayments.get(rowIndex).getPrice() :
                            null;
            }
            return null;
        }
    }

    private JTable table;
    private JTable subscriptionFeeRenderedServicesTable;
    private JTable subscriptionFeePaymentsTable;
    private JTable otherServicesTable;
    private JTable subscriberTariffsTable;
    private JTable subscriberSessionsTable;

    public SubscriberInformationView(SubscriberEntityModel model, Subscriber entity) {

        setTitle(getGuiString("window.subscriberInformation"));
        buildLayout();

        List<RenderedServiceAndPayment> renderedServiceAndPayments = new ArrayList<>();
        List<RenderedServiceExt> renderedServices = model.getRenderedServicesExtBySubscriberId(entity.getId());

        Integer balance = 0;
        for (RenderedServiceExt renderedService : renderedServices) {
            renderedServiceAndPayments.add(new RenderedServiceAndPayment(renderedService));
            balance += renderedService.getPrice();
        }
        List<PaymentExt> payments = model.getPaymentsExtBySubscriberId(entity.getId());
        for (PaymentExt payment : payments) {
            renderedServiceAndPayments.add(new RenderedServiceAndPayment(payment));
            balance -= payment.getPrice();
        }
        renderedServiceAndPayments.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));

        IntegerFormField accountFormField = new IntegerFormField("subscriber.id");
        accountFormField.setValue(entity.getAccount());
        addFormField(accountFormField);
        StringFormField nameFormField = new StringFormField("subscriber.name");
        nameFormField.setValue(entity.getName());
        addFormField(nameFormField);
        IntegerFormField balanceFormField = new IntegerFormField("subscriber.balance");
        balanceFormField.setValue(balance);
        addFormField(balanceFormField);

        JTabbedPane tabbedPane = new JTabbedPane();
        DefaultTableCellRenderer renderer;
        JScrollPane tableScrollPane;
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel subscriptionFeeChargeableAndPaidTablesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel subscriptionFeeRenderedServicesPanel = new JPanel(new BorderLayout());
        JPanel subscriptionFeePaymentsPanel = new JPanel(new BorderLayout());
        this.subscriptionFeeRenderedServicesTable = new JTable(
                new SubscriptionFeeRenderedServicesTableModel(
                        renderedServiceAndPayments.stream()
                                .filter(o -> o.getService().getId() == FixedServices.SUBSCRIPTION_FEE.getId())
                                .collect(Collectors.toList())));
        renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.subscriptionFeeRenderedServicesTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tableScrollPane = new JScrollPane(this.subscriptionFeeRenderedServicesTable);
        subscriptionFeeRenderedServicesPanel.add(new JLabel(getEntityString("subscriptionFee")), BorderLayout.NORTH);
        subscriptionFeeRenderedServicesPanel.add(tableScrollPane, BorderLayout.CENTER);
        subscriptionFeeChargeableAndPaidTablesPanel.add(subscriptionFeeRenderedServicesPanel);

        this.subscriptionFeePaymentsTable = new JTable(new PaymentsTableModel(payments.stream()
                .filter(paymentExt -> paymentExt.getService().getId() == FixedServices.SUBSCRIPTION_FEE.getId())
                .collect(Collectors.toList())));
        renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.subscriptionFeePaymentsTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tableScrollPane = new JScrollPane(this.subscriptionFeePaymentsTable);
        subscriptionFeePaymentsPanel.add(new JLabel(getEntityString("payments")), BorderLayout.NORTH);
        subscriptionFeePaymentsPanel.add(tableScrollPane, BorderLayout.CENTER);
        subscriptionFeeChargeableAndPaidTablesPanel.add(subscriptionFeePaymentsPanel);

        tabbedPane.add(getGuiString("tabbedPane.chargeableAndPaidTables") + " : " + getEntityString("subscriptionFee"),
                subscriptionFeeChargeableAndPaidTablesPanel);

        JPanel otherServicesChargeableAndPaidTablesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.otherServicesTable = new JTable(
                new OtherRenderedServicesTableModel(
                        renderedServiceAndPayments.stream()
                                .filter(o -> o.getService().getId() != FixedServices.SUBSCRIPTION_FEE.getId())
                                .collect(Collectors.toList())));
        renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.otherServicesTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tableScrollPane = new JScrollPane(this.otherServicesTable);
        otherServicesChargeableAndPaidTablesPanel.add(tableScrollPane);

        tabbedPane.add(getGuiString("tabbedPane.chargeableAndPaidTables") + " : " + getGuiString("additionalServices"),
                otherServicesChargeableAndPaidTablesPanel);

        JPanel subscriberTariffsAndSessionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel subscriberTariffsPanel = new JPanel(new BorderLayout());
        JPanel subscriberSessionsPanel = new JPanel(new BorderLayout());

        this.subscriberTariffsTable = new JTable(
                new TariffsTableModel(
                        model.getSubscriberTariffs(entity.getId())));
        renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.subscriberTariffsTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tableScrollPane = new JScrollPane(this.subscriberTariffsTable);
        subscriberTariffsPanel.add(new JLabel(getEntityString("tariffs")), BorderLayout.NORTH);
        subscriberTariffsPanel.add(tableScrollPane, BorderLayout.CENTER);
        subscriberTariffsAndSessionsPanel.add(subscriberTariffsPanel);

        this.subscriberSessionsTable = new JTable(
                new SessionsTableModel(
                        model.getSubscriberSessions(entity.getId())));
        renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.subscriberSessionsTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tableScrollPane = new JScrollPane(this.subscriberSessionsTable);
        subscriberSessionsPanel.add(new JLabel(getGuiString("sessions")), BorderLayout.NORTH);
        subscriberSessionsPanel.add(tableScrollPane, BorderLayout.CENTER);
        subscriberTariffsAndSessionsPanel.add(subscriberSessionsPanel);

        tabbedPane.add(getGuiString("tabbedPane.subscriberTariffsAndSessions"),
                subscriberTariffsAndSessionsPanel);

        rightPanel.add(tabbedPane);
        getContentPanel().add(rightPanel);

        revalidate();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);
    }
}
