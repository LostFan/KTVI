package org.lostfan.ktv.view.entity;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.RenderedServiceExt;
import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.view.FormView;

public class SubscriberEntityView extends EntityView {

    private static class SubscriberRenderedServicesAndPaymentsTableModel extends AbstractTableModel {

        private List<RenderedServiceAndPayment> renderedServiceAndPayments = new ArrayList<>();

        public SubscriberRenderedServicesAndPaymentsTableModel(List<RenderedServiceAndPayment> renderedServiceAndPayments) {
            this.renderedServiceAndPayments = renderedServiceAndPayments;
        }

        @Override
        public int getRowCount() {
            return renderedServiceAndPayments.size();
        }

        @Override
        public int getColumnCount() {
            return  5;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: return ResourceBundles.getGuiBundle().getString(
                        "number");
                case 1: return ResourceBundles.getEntityBundle().getString(
                        "renderedService.date");
                case 2: return ResourceBundles.getGuiBundle().getString(
                        "creditDebit");
                case 3: return ResourceBundles.getEntityBundle().getString(
                        "service");
                case 4: return ResourceBundles.getEntityBundle().getString(
                        "renderedService.price");
            }

            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0: return Integer.class;
                case 1: return LocalDate.class;
                case 2: return String.class;
                case 3: return Service.class;
                case 4: return Integer.class;
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
                case 0: return rowIndex + 1;
                case 1: return renderedServiceAndPayments.get(rowIndex).getDate();
                case 2: return renderedServiceAndPayments.get(rowIndex).isCredit() ?
                        ResourceBundles.getGuiBundle().getString("credit") :
                        ResourceBundles.getGuiBundle().getString("debit");
                case 3: return renderedServiceAndPayments.get(rowIndex).getService();
                case 4: return renderedServiceAndPayments.get(rowIndex).isCredit() ?
                        renderedServiceAndPayments.get(rowIndex).getPrice().negate() :
                        renderedServiceAndPayments.get(rowIndex).getPrice();
            }
            return null;
        }
    }

    private static class RenderedServiceAndPayment {

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

        private BigDecimal price;

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

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

    }

    private JTable table;

    public SubscriberEntityView(SubscriberEntityModel model) {
        super(model);
        FormView.FormField accountField = getFormField("subscriber.account");
        accountField.setValue(model.getNewSubscriberAccount());
        revalidate();
    }

    public SubscriberEntityView(SubscriberEntityModel model, Subscriber entity) {
        super(model, entity);

        List<RenderedServiceAndPayment> renderedServiceAndPayments = new ArrayList<>();
        List<RenderedServiceExt> renderedServices = model.getRenderedServicesExtBySubscriberId(entity.getId());

        BigDecimal balance = BigDecimal.ZERO;

        for (RenderedServiceExt renderedService : renderedServices) {
            renderedServiceAndPayments.add(new RenderedServiceAndPayment(renderedService));
            balance= balance.add(renderedService.getPrice());
        }
        List<PaymentExt> payments = model.getPaymentsExtBySubscriberId(entity.getId());
        for (PaymentExt payment : payments) {
            renderedServiceAndPayments.add(new RenderedServiceAndPayment(payment));
            balance=balance.add(payment.getPrice().negate());
        }
        renderedServiceAndPayments.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));

        BigDecimalFormField balanceFormField = new BigDecimalFormField("subscriber.balance");
        balanceFormField.setValue(balance);
        addFormField(balanceFormField);

        SubscriberRenderedServicesAndPaymentsTableModel subscriberRenderedServicesAndPaymentsTableModel = new SubscriberRenderedServicesAndPaymentsTableModel(renderedServiceAndPayments);
        this.table = new JTable(subscriberRenderedServicesAndPaymentsTableModel);
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setAutoCreateRowSorter(false);
        this.table.setFillsViewportHeight(true);
        getContentPanel().add(this.table);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        getContentPanel().add(tableScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        getContentPanel().add(rightPanel, BorderLayout.LINE_END);

        revalidate();
    }
}
