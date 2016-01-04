package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.utils.DefaultContextMenu;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
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
        private EntityComboBox comboBox;
        private JPanel panel;

        public SubscriberLabelFieldInput(Subscriber entity) {
            this.label = new JLabel(getEntityString("subscriber"), SwingConstants.LEFT);
            this.comboBox = EntityComboBoxFactory.createComboBox(EntityFieldTypes.Subscriber);

            new DefaultContextMenu().add((JTextField) this.comboBox.getEditor().getEditorComponent());
            this.comboBox.setEditable(true);
            if (entity != null) {

                Object value = entity;
                if(value != null) {
                    this.comboBox.setSelectedId(entity.getId());
                    value = this.comboBox.getSelectedEntity().getName();
                    ((JTextField) ((this.comboBox).getEditor().getEditorComponent())).setText((String) value);
                }
            }
            this.panel = new JPanel(new BorderLayout());
            this.panel.add(this.comboBox, BorderLayout.CENTER);

            // TODO: Move the following 2 buttons into the EntityComboBox component
            // and convert it to a JPanel that contains all these 3 components
            JButton tableButton = new JButton("...");
            tableButton.addActionListener(e -> {
                EntitySelectionView entitySelectionView;
                    entitySelectionView = EntitySelectionFactory.createForm(EntityFieldTypes.Subscriber);
                openDialog(entitySelectionView);
                if (entitySelectionView.get() != null) {
                    this.comboBox.setSelectedEntity(entitySelectionView.get());
                    ((JTextField) ((this.comboBox).getEditor().getEditorComponent())).setText(entitySelectionView.get().getName());
                    this.comboBox.invalidate();
                    this.comboBox.repaint();
                }
            });

            JButton entityButton = new JButton();
            URL url = EntitySearchView.class.getClassLoader().getResource("images/search.png");
            if(url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image image = icon.getImage().getScaledInstance(10,10,Image.SCALE_SMOOTH);
                icon = new ImageIcon(image);
                entityButton.setIcon(icon);
            }

            entityButton.addActionListener(e -> {
                if(this.comboBox.getSelectedEntity() != null) {
                    EntityView entityView = EntityViewFactory.createForm(EntityFieldTypes.Subscriber, comboBox.getSelectedEntity().getId());
                    entityView.changeActionListener.actionPerformed(null);
                }
            });

            JPanel buttonPanel = new JPanel(new BorderLayout(0, 0));
            buttonPanel.add(tableButton, BorderLayout.LINE_START);
            buttonPanel.add(entityButton, BorderLayout.LINE_END);
            this.panel.add(buttonPanel, BorderLayout.LINE_END);
        }


        public JComponent getInputComponent() {
            return this.panel;
        }


        public Object getValue() {
            return  this.comboBox.getSelectedEntity() != null ? this.comboBox.getSelectedEntity().getId() : null;
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


    private static final int WIDTH = 450;
    private static final int HEIGHT = 550;

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
        setSize(WIDTH, HEIGHT);

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
