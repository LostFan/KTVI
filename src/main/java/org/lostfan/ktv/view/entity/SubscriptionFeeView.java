package org.lostfan.ktv.view.entity;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.SubscriptionFeeRecalculationModel;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.EntityInnerTableView;
import org.lostfan.ktv.view.FrameView;
import org.lostfan.ktv.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SubscriptionFeeView extends FrameView {

    private class DateLabelFieldInput {

        private JLabel label;
        private DatePickerField datePicker;

        public DateLabelFieldInput() {
            this.label = new JLabel(getEntityString("renderedService.date"), SwingConstants.LEFT);
            this.datePicker = new DatePickerField();
        }

        public DatePickerField getInputComponent() {
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

    private class ModelObserver implements org.lostfan.ktv.utils.Observer {

        @Override
        public void update(Object args) {
            Integer progress = subscriptionFeeRecalculationModel.getProgress();
            if (progress != null) {
                SubscriptionFeeView.this.progressBar.setValue(progress);
                if (progress == 100) {
                    entityInnerTableView.getEntityList().clear();
                    entityInnerTableView.getEntityList().addAll(subscriptionFeeRecalculationModel.getNewRenderedServices());
                }
            }
            SubscriptionFeeView.this.revalidate();
        }
    }

    private DateLabelFieldInput dateLabelFieldInput;
    private SubscriberLabelFieldInput subscriberLabelFieldInput;
    private JButton recalculateButton;
    private JButton addButton;
    private JButton cancelButton;
    private JPanel fieldPanel;
    private JProgressBar progressBar;
    private EntityInnerTableView<RenderedService> entityInnerTableView;
    private ModelObserver modelObserver;

    private SubscriptionFeeModel model;
    private SubscriptionFeeRecalculationModel subscriptionFeeRecalculationModel;

    private ViewActionListener recalculateActionListener;
    private ViewActionListener cancelActionListener;
    private ViewActionListener addActionListener;

    private boolean subscriberVision;

    public SubscriptionFeeView(SubscriptionFeeModel model, SubscriptionFeeRecalculationModel subscriptionFeeRecalculationModel, boolean subscriberVision) {
        this.subscriberVision = subscriberVision;
        this.model = model;
        this.subscriptionFeeRecalculationModel = subscriptionFeeRecalculationModel;

        modelObserver = new ModelObserver();

        setTitle(getEntityString(model.getEntityNameKey()));

        this.recalculateButton = new JButton(getGuiString("buttons.recalculate"));
        this.recalculateButton.addActionListener(e -> {

            if (this.recalculateActionListener != null) {
                if (subscriberLabelFieldInput == null) {
                    new Thread(() -> {
                        this.recalculateButton.setEnabled(false);
                        this.addButton.setEnabled(false);
                        subscriptionFeeRecalculationModel.addObserver(this.modelObserver);
                        this.recalculateActionListener.actionPerformed(dateLabelFieldInput.getValue());
                        subscriptionFeeRecalculationModel.removeObserver(this.modelObserver);
                        this.recalculateButton.setEnabled(true);
                        this.addButton.setEnabled(true);
                        this.entityInnerTableView.getTable().revalidate();
                    }).start();
                } else {
                    this.recalculateActionListener.actionPerformed(new DateAndSubscriberId((Integer) subscriberLabelFieldInput.getValue(), (LocalDate) dateLabelFieldInput.getValue()));
                    this.addButton.setEnabled(true);
                    entityInnerTableView.getEntityList().clear();
                    entityInnerTableView.getEntityList().addAll(subscriptionFeeRecalculationModel.getNewRenderedServices());
                    entityInnerTableView.getTable().repaint();
                    entityInnerTableView.getTable().revalidate();
                }
            }
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

        dateLabelFieldInput = new DateLabelFieldInput();
        dateLabelFieldInput.getInputComponent().addActionListener(e -> {
            addButton.setEnabled(false);
        });
        if(this.subscriberVision) {
            subscriberLabelFieldInput = new SubscriberLabelFieldInput(null);
        }

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);



        this.entityInnerTableView = new EntityInnerTableView<>(model.getLoadFullEntityField(), new ArrayList<>());

        buildLayout(subscriberVision);

        show();
    }

    private void buildLayout(boolean subscriberVision) {

        if(subscriberVision) {
            progressBar.setVisible(false);
        }
        addButton.setEnabled(false);
        this.entityInnerTableView.setVisibleButtons(false);
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


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        panel.add(this.entityInnerTableView.getContentPanel());
        this.entityInnerTableView.getTable().setEnabled(false);
        getContentPanel().add(panel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(recalculateButton);
        buttonPanel.add(addButton);
        buttonPanel.add(progressBar);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setRecalculateActionListener(ViewActionListener recalculateActionListener) {
        this.recalculateActionListener = recalculateActionListener;
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void setCancelActionListener(ViewActionListener cancelActionListener) {
        this.cancelActionListener = cancelActionListener;
    }

    public void showErrors(java.util.List<org.lostfan.ktv.validation.Error> errors) {
        // TODO: add appropriate implementation of visualising the errors
        StringBuffer message = new StringBuffer();
        for (Error error : errors) {
            message.append(error.getField() != null ? getEntityString(error.getField()) + " " : "");
            String err = getGuiString(error.getMessage());
            if (error.getParams().length != 0) {
                err = String.format(err, error.getParams());
            }
            message.append(err);
            message.append("\n");
        }
        JOptionPane.showMessageDialog(getFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
