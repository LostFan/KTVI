package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class SubscriptionFeeView extends FrameView {

    private class DateLabelFieldInput {

        private JLabel label;
        private DatePickerField datePicker;

        public DateLabelFieldInput() {
            this.label = new JLabel(getEntityString("renderedService.date"), SwingConstants.LEFT);
            this.datePicker = new DatePickerField();
        }

        public JComponent getInputComponent() {
            return this.datePicker;
        }

        public LocalDate getValue() {
            return this.datePicker.getValue();
        }
    }

    private class SubscriberLabelFieldInput {

        private JLabel label;
        private EntityPanel panel;

        public SubscriberLabelFieldInput(Subscriber entity) {
            this.label = new JLabel(getEntityString("subscriber"), SwingConstants.LEFT);
            this.panel = EntityPanelFactory.createEntityPanel(EntityFieldTypes.Subscriber);

            this.panel.setParentView(SubscriptionFeeView.this);

            if (entity != null) {

                Object value = entity;
                if(value != null) {
                    this.panel.setSelectedId(entity.getId());
                    value = this.panel.getSelectedEntity().getName();
                    ((JTextField) ((this.panel.getComboBox()).getEditor().getEditorComponent())).setText((String) value);
                }
            }
        }


        public JComponent getInputComponent() {
            return this.panel;
        }


        public Object getValue() {
            return  this.panel.getSelectedEntity() != null ? this.panel.getSelectedEntity().getId() : null;
        }
    }

    public class DateAndSubscriberId {

        private LocalDate date;
        private Integer subscriberId;

        public DateAndSubscriberId(Integer subscriberId, LocalDate date) {
            this.subscriberId = subscriberId;
            this.date = date;
        }

        public LocalDate getDate() {
            return date;
        }

        public Integer getSubscriberId() {
            return subscriberId;
        }
    }

    private DateLabelFieldInput dateLabelFieldInput;
    private SubscriberLabelFieldInput subscriberLabelFieldInput;
    private JButton addButton;
    private JButton cancelButton;
    private JPanel fieldPanel;

    private SubscriptionFeeModel model;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;
    private ViewActionListener changeActionListener;

    private boolean subscriberVision;

    public SubscriptionFeeView(SubscriptionFeeModel model) {
        this(model, false);
    }

    public SubscriptionFeeView(SubscriptionFeeModel model, boolean subscriberVision) {
        this.subscriberVision = subscriberVision;
        this.model = model;

        setTitle(getEntityString(model.getEntityNameKey()));

        this.addButton = new JButton(getGuiString("buttons.count"));
        this.addButton.addActionListener(e -> {

            if (this.addActionListener != null) {
                if(subscriberLabelFieldInput == null) {
                    this.addActionListener.actionPerformed(dateLabelFieldInput.getValue());
                } else {
                    this.addActionListener.actionPerformed(new DateAndSubscriberId((Integer) subscriberLabelFieldInput.getValue(), (LocalDate) dateLabelFieldInput.getValue()));
                }
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            if (this.cancelActionListener != null) {
                this.cancelActionListener.actionPerformed(null);
            }
            hide();
        });

        dateLabelFieldInput = new DateLabelFieldInput();
        if(this.subscriberVision) {
            subscriberLabelFieldInput = new SubscriberLabelFieldInput(null);
        }

        buildLayout();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        this.fieldPanel = new JPanel(new GridBagLayout());
        getContentPanel().add(this.fieldPanel, BorderLayout.PAGE_START);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 0;
        c.gridx = 0;
        this.fieldPanel.add(this.dateLabelFieldInput.label, c);
        c.gridx = 1;
        JComponent inputComponent = this.dateLabelFieldInput.getInputComponent();
        inputComponent.setMinimumSize(inputComponent.getPreferredSize());
        this.fieldPanel.add(inputComponent, c);
        if(this.subscriberVision) {
            c.gridy = 1;
            c.gridx = 0;
            this.fieldPanel.add(this.subscriberLabelFieldInput.label, c);
            c.gridx = 1;
            inputComponent = this.subscriberLabelFieldInput.getInputComponent();
            inputComponent.setMinimumSize(inputComponent.getPreferredSize());
            this.fieldPanel.add(inputComponent, c);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void setCancelActionListener(ViewActionListener cancelActionListener) {
        this.cancelActionListener = cancelActionListener;
    }

    public void showErrors(java.util.List<org.lostfan.ktv.validation.Error> errors) {
        // TODO: add appropriate implementation of visualising the errors
        for (Error error : errors) {
            String message = error.getField() != null ? getEntityString(error.getField()) + " " : "";
            if (error.getMessage().equals("empty")) {
                message += "should not be empty";
            } else {
                message += error.getMessage();
            }
            JOptionPane.showMessageDialog(getFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
