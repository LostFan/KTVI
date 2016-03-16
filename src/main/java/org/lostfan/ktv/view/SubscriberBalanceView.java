package org.lostfan.ktv.view;

import java.awt.*;
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

public class SubscriberBalanceView extends FormView {

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
            return 5;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundles.getGuiBundle().getString(
                            "number");
                case 1:
                    return ResourceBundles.getEntityBundle().getString(
                            "renderedService.date");
                case 2:
                    return ResourceBundles.getGuiBundle().getString(
                            "creditDebit");
                case 3:
                    return ResourceBundles.getEntityBundle().getString(
                            "service");
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
                    return Integer.class;
                case 1:
                    return LocalDate.class;
                case 2:
                    return String.class;
                case 3:
                    return Service.class;
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

            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return renderedServiceAndPayments.get(rowIndex).getDate();
                case 2:
                    return renderedServiceAndPayments.get(rowIndex).isCredit() ?
                            ResourceBundles.getGuiBundle().getString("credit") :
                            ResourceBundles.getGuiBundle().getString("debit");
                case 3:
                    return renderedServiceAndPayments.get(rowIndex).getService();
                case 4:
                    return renderedServiceAndPayments.get(rowIndex).isCredit() ?
                            renderedServiceAndPayments.get(rowIndex).getPrice() * -1 :
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

    private JTable table;

    public SubscriberBalanceView(SubscriberEntityModel model, Subscriber entity) {

        setTitle(getGuiString("window.balance"));
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


        IntegerFormField balanceFormField = new IntegerFormField("subscriber.balance");
        balanceFormField.setValue(balance);
        addFormField(balanceFormField);


        SubscriberRenderedServicesAndPaymentsTableModel subscriberRenderedServicesAndPaymentsTableModel = new SubscriberRenderedServicesAndPaymentsTableModel(renderedServiceAndPayments);
        this.table = new JTable(subscriberRenderedServicesAndPaymentsTableModel);
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        System.out.println(getFrame().getContentPane().getHeight());
        this.table.setAutoCreateRowSorter(false);
        this.table.setFillsViewportHeight(true);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightPanel.add(tableScrollPane);
        getContentPanel().add(rightPanel);

        revalidate();


        show();
        if (getFrame().getContentPane().getWidth() > 300 && getFrame().getContentPane().getHeight() > 200) {
            this.table.setPreferredScrollableViewportSize(new Dimension(
                    getFrame().getContentPane().getWidth() - 90,
                    getFrame().getContentPane().getHeight() - 120));
        }
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);

    }

}