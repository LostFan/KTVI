package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.*;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class EntityView extends FrameView {

    protected abstract class LabelFieldInput {

        protected JLabel label;

        protected EntityField entityField;

        public LabelFieldInput(EntityField entityField) {
            this.label = new JLabel(getEntityString(entityField.getTitleKey()), SwingConstants.LEFT);
            this.entityField = entityField;
        }

        /**
         * Returns a Component that represents the source for entering a value
         */
        public abstract JComponent getInputComponent();

        /**
         * Returns the current value of the component
         */
        public abstract Object getValue();

        protected EntityField getEntityField() {
            return this.entityField;
        }
    }

    private class StringLabelFieldInput extends LabelFieldInput {

        private JTextField textField;

        public StringLabelFieldInput(EntityField entityField) {
            super(entityField);
            this.textField = new JTextField(20);
            this.textField.setMargin(new Insets(2, 6, 2, 6));
            if (EntityView.this.entity != null) {
                this.textField.setText(String.valueOf(entityField.get(EntityView.this.entity)));
            }
            new DefaultContextMenu().add(textField);
        }

        @Override
        public JComponent getInputComponent() {
            return this.textField;
        }

        @Override
        public Object getValue() {
            return this.textField.getText();
        }
    }

    private class IntegerLabelFieldInput extends LabelFieldInput {

        private IntegerTextField textField;

        public IntegerLabelFieldInput(EntityField entityField) {
            super(entityField);
            this.textField = new IntegerTextField();
            this.textField.setMargin(new Insets(2, 6, 2, 6));
            if (EntityView.this.entity != null) {
                this.textField.setValue((Integer)entityField.get(EntityView.this.entity));
            }

            new DefaultContextMenu().add(textField);
        }

        @Override
        public JComponent getInputComponent() {
            return this.textField;
        }

        @Override
        public Object getValue() {
            return textField.getValue();
        }
    }

    private class DoubleLabelFieldInput extends LabelFieldInput {

        private JTextField textField;

        public DoubleLabelFieldInput(EntityField entityField) {
            super(entityField);
            // TODO: Implement FormattedTextField or another way to filter double values only
            this.textField = new JTextField(20);
            this.textField.setMargin(new Insets(2, 6, 2, 6));
            if (EntityView.this.entity != null) {
                this.textField.setText(String.valueOf(entityField.get(EntityView.this.entity)));
            }
            new DefaultContextMenu().add(textField);
        }

        @Override
        public JComponent getInputComponent() {
            return this.textField;
        }

        @Override
        public Object getValue() {
            String text = textField.getText();
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException | NullPointerException e) {
                return null;
            }
        }
    }

    private class BooleanLabelFieldInput extends LabelFieldInput {

        private JCheckBox checkBox;

        public BooleanLabelFieldInput(EntityField entityField) {
            super(entityField);
            this.checkBox = new JCheckBox();
            if (EntityView.this.entity != null) {
                checkBox.setSelected((Boolean)entityField.get(EntityView.this.entity));
            }
        }

        @Override
        public JComponent getInputComponent() {
            return this.checkBox;
        }

        @Override
        public Object getValue() {
            return this.checkBox.isSelected();
        }
    }

    private class DateLabelFieldInput extends LabelFieldInput {

        private DatePickerField datePicker;

        public DateLabelFieldInput(EntityField entityField) {
            super(entityField);
            this.datePicker = new DatePickerField();
            if (EntityView.this.entity != null) {
                this.datePicker.setValue((LocalDate) entityField.get(EntityView.this.entity));
            }
        }

        @Override
        public JComponent getInputComponent() {
            return this.datePicker;
        }

        @Override
        public Object getValue() {
            return this.datePicker.getValue();
        }
    }

    protected class EntityLabelFieldInput extends LabelFieldInput {

        private EntityComboBox comboBox;
        private JPanel panel;

        public EntityLabelFieldInput(EntityField entityField, Entity entity) {
            this(entityField, entity, null, null);
        }

        public EntityLabelFieldInput(EntityField entityField, Entity entity, EntityComboBox jComboBox, final Supplier entitySelView ) {
            super(entityField);

            if(jComboBox == null) {
                this.comboBox = EntityComboBoxFactory.createComboBox(entityField.getType());
            } else {
                this.comboBox = jComboBox;
            }

            new DefaultContextMenu().add((JTextField) this.comboBox.getEditor().getEditorComponent());
            this.comboBox.setEditable(true);
            if (entity != null) {

                Object value = entityField.get(entity);
                if(value != null) {
                    this.comboBox.setSelectedId((Integer) value);
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
                if(entitySelView == null) {
                    entitySelectionView = EntitySelectionFactory.createForm(entityField.getType());
                } else {
                    entitySelectionView = (EntitySelectionView) entitySelView.get();
                }
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
                    EntityView entityView = EntityViewFactory.createForm(entityField.getType(), comboBox.getSelectedEntity().getId());
                    entityView.changeActionListener.actionPerformed(null);
                }
            });

            JPanel buttonPanel = new JPanel(new BorderLayout(0, 0));
            buttonPanel.add(tableButton, BorderLayout.LINE_START);
            buttonPanel.add(entityButton, BorderLayout.LINE_END);
            this.panel.add(buttonPanel, BorderLayout.LINE_END);
        }

        @Override
        public JComponent getInputComponent() {
            return this.panel;
        }

        @Override
        public Object getValue() {
            return  this.comboBox.getSelectedEntity() != null ? this.comboBox.getSelectedEntity().getId() : null;
        }
    }

    public static final int WIDTH = 500;
    public static final int HEIGHT = 550;

    private List<LabelFieldInput> labelFieldInputs;
    private JButton addButton;
    private JButton cancelButton;
    private JPanel fieldPanel;
    private EntityInnerTableView innerTableView;

    private EntityModel<? extends Entity> model;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;
    protected ViewActionListener changeActionListener;

    private Entity entity;

    public EntityView(EntityModel model) {
        this(model, null);
    }

    public <E extends Entity> EntityView(EntityModel<Entity> model, Entity entity) {
        this.entity = entity;
        this.model = model;

        setTitle(getEntityString(model.getEntityNameKey()));
        setSize(WIDTH, HEIGHT);

        this.addButton = new JButton(getGuiString(entity == null ? "buttons.add" : "buttons.change"));
        this.addButton.addActionListener(e -> {
            if (this.addActionListener != null) {
                // Stop to edit the inner table view
                if (this.innerTableView != null) {
                    this.innerTableView.stopEditing();
                }
                this.addActionListener.actionPerformed(buildEntity());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            if (this.cancelActionListener != null) {
                this.cancelActionListener.actionPerformed(null);
            }
            hide();
        });

        labelFieldInputs = new ArrayList<>();
        for (EntityField entityField : model.getFields()) {
            if (!entityField.isEditable()) {
                continue;
            }
            labelFieldInputs.add(createLabelFieldInput(entityField));
        }

        buildLayout();

        show();
    }

    protected JPanel getFieldPanel() {
        return this.fieldPanel;
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        this.fieldPanel = new JPanel(new GridBagLayout());
        getContentPanel().add(this.fieldPanel, BorderLayout.PAGE_START);

        for (int i = 0; i < this.labelFieldInputs.size(); i++) {
            addLabelFieldInput(this.labelFieldInputs.get(i));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    private LabelFieldInput createLabelFieldInput(EntityField entityField) {
        switch (entityField.getType()) {
            case String:
                return new StringLabelFieldInput(entityField);
            case Integer:
                return new IntegerLabelFieldInput(entityField);
            case Double:
                return new DoubleLabelFieldInput(entityField);
            case Boolean:
                return new BooleanLabelFieldInput(entityField);
            case Date:
                return new DateLabelFieldInput(entityField);
            default:
                return entityField.getType().isEntityClass() ? new EntityLabelFieldInput(entityField , this.entity) : new StringLabelFieldInput(entityField);
        }
    }

    protected LabelFieldInput createLabelFieldInput(EntityField entityField, Entity entity) {
        switch (entityField.getType()) {
            case String:
                return new StringLabelFieldInput(entityField);
            case Integer:
                return new IntegerLabelFieldInput(entityField);
            case Double:
                return new DoubleLabelFieldInput(entityField);
            case Boolean:
                return new BooleanLabelFieldInput(entityField);
            case Date:
                return new DateLabelFieldInput(entityField);
            default:
                return entityField.getType().isEntityClass() ? new EntityLabelFieldInput(entityField , entity) : new StringLabelFieldInput(entityField);
        }
    }

    protected void addLabelFieldInput(LabelFieldInput input) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = this.fieldPanel.getComponentCount();
        c.gridx = 0;
        this.fieldPanel.add(input.label, c);
        c.gridx = 1;
        JComponent inputComponent = input.getInputComponent();
        // HACK: textFields is excessively narrow when it's smaller than the displayed area.
        inputComponent.setMinimumSize(inputComponent.getPreferredSize());
        this.fieldPanel.add(inputComponent, c);
    }

    /**
     * Assembles values of the input fields into a Entity object.
     */
    protected Entity buildEntity() {
        Entity entity = this.entity;
        if (entity == null) {
            entity = this.model.createNewEntity();
        }

        for (LabelFieldInput labelFieldInput : this.labelFieldInputs) {
            labelFieldInput.entityField.set(entity, labelFieldInput.getValue());
        }

        return entity;
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void setCancelActionListener(ViewActionListener cancelActionListener) {
        this.cancelActionListener = cancelActionListener;
    }

    public void showErrors(List<org.lostfan.ktv.validation.Error> errors) {
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

    public void setChangeActionListener(ViewActionListener changeActionListener) {
        this.changeActionListener = changeActionListener;
    }

    protected void setInnerTable(EntityInnerTableView innerTableView) {
        this.innerTableView = innerTableView;
        getContentPanel().add(innerTableView.getContentPanel(), BorderLayout.CENTER);
    }
}
