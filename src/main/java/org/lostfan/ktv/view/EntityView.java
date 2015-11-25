package org.lostfan.ktv.view;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.*;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.components.EntityComboBox;
import org.lostfan.ktv.view.components.EntityComboBoxFactory;
import org.lostfan.ktv.view.components.EntityViewFactory;
import org.lostfan.ktv.view.components.EntitySelectionFactory;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class EntityView {

    protected abstract class LabelFieldInput {

        protected JLabel label;

        private EntityField entityField;

        public LabelFieldInput(EntityField entityField) {
            this.label = new JLabel(ResourceBundles.getEntityBundle().getString(entityField.getTitleKey()), SwingConstants.LEFT);
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

        private JTextField textField;

        public IntegerLabelFieldInput(EntityField entityField) {
            super(entityField);
            // TODO: Implement FormattedTextField or another way to filter integer only
            this.textField = new JTextField(/*20*/);
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
            if (text == null) {
                return null;
            }
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return null;
            }
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

        private JDatePickerImpl datePicker;

        public DateLabelFieldInput(EntityField entityField) {
            super(entityField);
            this.datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()), new DateLabelFormatter());
            if (EntityView.this.entity != null) {
                LocalDate localDate = (LocalDate) entityField.get(EntityView.this.entity);
                this.datePicker.getModel().setDate(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
                this.datePicker.getModel().setSelected(true);
            }
        }

        @Override
        public JComponent getInputComponent() {
            return this.datePicker;
        }

        @Override
        public Object getValue() {
            return new java.sql.Date(((Date)datePicker.getModel().getValue()).getTime()).toLocalDate();
        }
    }

    private class EntityLabelFieldInput extends LabelFieldInput {

        private EntityComboBox comboBox;
        private JPanel panel;

        public EntityLabelFieldInput(EntityField entityField) {
            super(entityField);
            this.comboBox = EntityComboBoxFactory.createComboBox(entityField.getType());
            new DefaultContextMenu().add((JTextField) comboBox.getEditor().getEditorComponent());
            comboBox.setEditable(true);
            if (EntityView.this.entity != null) {

                Object value = entityField.get(EntityView.this.entity);
                comboBox.setSelectedId((Integer) value);
                value = comboBox.getSelectedEntity().getName();
                ((JTextField)((comboBox).getEditor().getEditorComponent())).setText((String) value);
            }
            this.panel = new JPanel(new BorderLayout());
            this.panel.add(comboBox, BorderLayout.CENTER);

            // TODO: Move the following 2 buttons into the EntityComboBox component
            // and convert it to a JPanel that contains all these 3 components
            JButton tableButton = new JButton("...");
            tableButton.addActionListener(e -> {
                EntitySelectionView entitySelectionView = EntitySelectionFactory.createForm(entityField.getType());
                if (entitySelectionView.getSelectedEntity() != null) {
                    comboBox.setSelectedEntity(entitySelectionView.getSelectedEntity());
                    ((JTextField) ((comboBox).getEditor().getEditorComponent())).setText(entitySelectionView.getSelectedEntity().getName());
                    comboBox.invalidate();
                    comboBox.repaint();
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
//                EntityModel entityModel = EntityModelFactory.createForm(entityField.getType());
                if(comboBox.getSelectedEntity() != null) {
                    EntityView entityView = EntityViewFactory.createForm(entityField.getType(), comboBox.getSelectedEntity().getId());
//                entityView.addObserver(comboBox.getModelObserver());
//                EntityView entityView = new EntityView(entityModel, entityModel.getEntity(comboBox.getSelectedEntity()));
//                EntityOneController entityOneController = new EntityOneController(entityModel, entityView);
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
            return this.comboBox.getSelectedEntity().getId();
        }
    }

    protected static final int WIDTH = 450;
    protected static final int HEIGHT = 550;

    protected JFrame frame;
    protected List<LabelFieldInput> labelFieldInputs;
    protected JButton addButton;
    protected JButton cancelButton;
    protected EntityModel<? extends Entity> model;
    private Map<String, List<Entity>> entityInnerTableValues;

    protected ViewActionListener addActionListener;
    protected ViewActionListener cancelActionListener;
    protected ViewActionListener changeActionListener;

    protected Entity entity;

    public EntityView(EntityModel model) {
        this(model, null);
    }

    public <E extends Entity> EntityView(EntityModel<Entity> model, Entity entity) {
//        System.out.println(((MaterialConsumption)((List)model.getFullFields().get(0).get(entity)).get(0)).getAmount());


        this.entity = entity;
        this.model = model;

        this.frame = new JFrame(ResourceBundles.getEntityBundle().getString(model.getEntityNameKey()));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setLocationRelativeTo(null);



        this.addButton = new JButton(getString(entity == null ? "buttons.add" : "buttons.change"));
        this.addButton.addActionListener(e -> {

            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(model.buildDTO(getEntity(), entityInnerTableValues));
            }
        });

        this.cancelButton = new JButton(getString("buttons.cancel"));
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

        this.entityInnerTableValues = new HashMap<>();
        for (FullEntityField fullEntityField : model.getFullFields()) {
            List<Entity> list = (List<Entity>) fullEntityField.get(entity);
//                System.out.println(((MaterialConsumption)((List)model.getFullFields().get(0).get(entity)).get(0)).getAmount());
            EntityInnerTableView entityInnerTableView = new EntityInnerTableView(fullEntityField, list);
            this.entityInnerTableValues.put(fullEntityField.getTitleKey(), entityInnerTableView.getEntityList());
            this.addInnerTable(entityInnerTableView);
        }

        frame.setVisible(true);
    }

    private void buildLayout() {

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        frame.add(contentPanel, BorderLayout.PAGE_START);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < this.labelFieldInputs.size(); i++) {
            c.gridy = i;
            c.gridx = 0;
            contentPanel.add(this.labelFieldInputs.get(i).label, c);
            c.gridx = 1;
            JComponent inputComponent = this.labelFieldInputs.get(i).getInputComponent();
            // HACK: textFields is excessively narrow when it's smaller than the displayed area.
            inputComponent.setMinimumSize(inputComponent.getPreferredSize());
            contentPanel.add(inputComponent, c);
        }

//        List<EntityModel> entityModels = model.getTableModels();
//        entityInnerTableViewList = new ArrayList<>();
//        if(entityModels != null) {
//            for (EntityModel entityModel : entityModels) {
//                EntityInnerTableView entityInnerTableView =
//                        new EntityInnerTableView(entityModel, this.entity == null ? null : this.entity.getId());
//                entityInnerTableViewList.add(entityInnerTableView);
//                frame.add(entityInnerTableView.getContentPanel(), BorderLayout.CENTER);
//            }
//        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
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
                return entityField.getType().isEntityClass() ? new EntityLabelFieldInput(entityField) : new StringLabelFieldInput(entityField);
        }
    }

    public Entity getEntity() {
        Entity entity = this.entity;
        if (entity == null) {
            entity = this.model.createNewEntity();
        }

        for (LabelFieldInput labelFieldInput : this.labelFieldInputs) {
            labelFieldInput.entityField.set(entity, labelFieldInput.getValue());
        }

        return entity;
    }

    public Map<EntityModel, List<Entity>> getTableEntities() {
        Map<EntityModel, List<Entity>> lists = new HashMap<>();
//        for (EntityInnerTableView entityInnerTableView : entityInnerTableViewList) {
//            lists.put(entityInnerTableView.getEntityModel(),entityInnerTableView.getEntityList());
//        }
//        Entity entity = this.entity;
//        if (entity == null) {
//            entity = this.model.createNewEntity();
//        }
//
//        for (LabelFieldInput labelFieldInput : this.labelFieldInputs) {
//            labelFieldInput.entityField.set(entity, labelFieldInput.getValue());
//        }

        return lists;
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
            String message = error.getField() != null ? ResourceBundles.getEntityBundle().getString(error.getField()) + " " : "";
            if (error.getMessage().equals("empty")) {
                message += "should not be empty";
            } else {
                message += error.getMessage();
            }
            JOptionPane.showMessageDialog(this.frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setChangeActionListener(ViewActionListener changeActionListener) {
        this.changeActionListener = changeActionListener;
    }

    public void addInnerTable(EntityInnerTableView innerTableView) {
        frame.add(innerTableView.getContentPanel(), BorderLayout.CENTER);
    }

    public void hide() {
        this.frame.setVisible(false);
    }

    public void show() {
        this.frame.setVisible(true);
    }

    private String getString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
