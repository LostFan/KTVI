package org.lostfan.ktv.view.entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.PaymentEntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.EntityInnerTableView;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.components.EntityComboBoxModel;
import org.lostfan.ktv.view.components.EntityPanel;
import org.lostfan.ktv.view.components.EntityPanelFactory;

public class PaymentView extends FormView {

    private class SubscriberFormField extends FormField<Integer> {

        private EntityPanel panel;

        public SubscriberFormField(String fieldKey) {
            super(fieldKey);
            this.panel = EntityPanelFactory.createEntityPanel(EntityFieldTypes.Subscriber);

            this.panel.setParentView(PaymentView.this);
            ((EntityComboBoxModel)this.panel.getComboBox().getModel()).addEntitySelectedListener(e -> fireValueChanged(getValue()));
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
            if(value != null) {
                this.panel.setSelectedId(value);
                ((JTextField) ((this.panel.getComboBox()).getEditor().getEditorComponent()))
                        .setText(this.panel.getSelectedEntity().getName());
            }
        }
    }

    private EntityInnerTableView<Payment> entityInnerTableView;
    private PaymentEntityModel model;
    private JButton addButton;
    private JButton cancelButton;
    private DateFormField dateFormField;
    private SubscriberFormField subscriberFormField;
    private IntegerFormField priceFormField;
    private StringFormField bankFileNameFormField;

    protected ViewActionListener addActionListener;
    protected ViewActionListener cancelActionListener;
    protected ViewActionListener addPaymentsInTableListener;

    public PaymentView(PaymentEntityModel model) {
        this(model, null);
    }

    public PaymentView(PaymentEntityModel model, Payment entity) {
        this.model = model;
        setTitle(getEntityString("payments"));
        dateFormField = new DateFormField("payment.payDate");
        subscriberFormField = new SubscriberFormField("subscriber");
        priceFormField = new IntegerFormField("payment.price");
        bankFileNameFormField = new StringFormField("payment.bankFileName");
        addFormField(dateFormField);
        addFormField(subscriberFormField);
        addFormField(priceFormField);
        addFormField(bankFileNameFormField);

        this.entityInnerTableView = new EntityInnerTableView<>(model.getLoadFullEntityField(), new ArrayList<>());

        if(entity != null) {
            dateFormField.setValue(entity.getDate());
            bankFileNameFormField.setValue(entity.getBankFileName());
            subscriberFormField.setValue(entity.getSubscriberAccount());
            List<Payment> payments = model.getList(entity.getSubscriberAccount(),entity.getDate(),entity.getBankFileName());
            Integer allPrice = 0;
            for (Payment payment : payments) {
                allPrice += payment.getPrice();
            }
            priceFormField.setValue(allPrice);
            this.entityInnerTableView.getEntityList().addAll(payments);
        }

        subscriberFormField.addValueListener(e -> {
            PaymentView.this.actionPerformedAddPaymentsInTableListener();
        });
        priceFormField.addValueListener(e -> {
            PaymentView.this.actionPerformedAddPaymentsInTableListener();
        });
        dateFormField.addValueListener(e -> {
            PaymentView.this.actionPerformedAddPaymentsInTableListener();
        });


        this.addButton = new JButton(getGuiString("buttons.add"));
        this.addButton.addActionListener(e -> {
            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(entityInnerTableView.getEntityList());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            if (this.cancelActionListener != null) {
                this.cancelActionListener.actionPerformed(null);
            }
            hide();
        });



        buildLayout();

        show();
    }

    private Payment createPayment() {
        Payment payment = new Payment();
        payment.setPrice(priceFormField.getValue());
        payment.setDate(dateFormField.getValue());
        payment.setSubscriberAccount(subscriberFormField.getValue());
        payment.setBankFileName(bankFileNameFormField.getValue());
        return payment;
    }

    private void actionPerformedAddPaymentsInTableListener() {
        if (PaymentView.this.addPaymentsInTableListener != null
                && priceFormField.getValue() != null && dateFormField.getValue() != null
                && subscriberFormField.getValue() != null) {
            PaymentView.this.addPaymentsInTableListener.actionPerformed(createPayment());
        }
    }

    private void buildLayout() {

        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        panel.add(this.entityInnerTableView.getContentPanel());
        getContentPanel().add(panel);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addPayments(List<Payment> payments) {
        entityInnerTableView.getEntityList().clear();
        entityInnerTableView.getEntityList().addAll(payments);
        entityInnerTableView.revalidate();
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }


    public void setAddPaymentsInTableListener(ViewActionListener addPaymentsInTable) {
        this.addPaymentsInTableListener = addPaymentsInTable;
    }

}
