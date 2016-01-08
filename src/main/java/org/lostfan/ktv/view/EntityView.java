package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.*;
import org.lostfan.ktv.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

public class EntityView extends FormView {

  protected class EntityFormField extends FormField<Integer> {

        private EntityComboBox comboBox;
        private JPanel panel;

        public EntityFormField(EntityField entityField, Entity entity) {
            this(entityField, entity, null, null);
        }

        public EntityFormField(EntityField entityField, Entity entity, EntityComboBox jComboBox, final Supplier entitySelView ) {
            super(entityField.getTitleKey());

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
      public void setValue(Integer value) {

      }

      @Override
        public Integer getValue() {
            return  this.comboBox.getSelectedEntity() != null ? this.comboBox.getSelectedEntity().getId() : null;
        }
    }

    public static final int WIDTH = 500;
    public static final int HEIGHT = 550;

    private JButton addButton;
    private JButton cancelButton;
    private EntityInnerTableView innerTableView;

    private EntityModel<? extends Entity> model;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;
    protected ViewActionListener changeActionListener;

    private Entity entity;
    private Map<EntityField, FormField> entityFormFieldMap;

    public EntityView(EntityModel model) {
        this(model, null);
    }

    public <E extends Entity> EntityView(EntityModel<Entity> model, Entity entity) {
        this.entity = entity;
        this.model = model;

        setTitle(getEntityString(model.getEntityNameKey()));
        setSize(WIDTH, HEIGHT);

        entityFormFieldMap = new HashMap<>();

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

        for (EntityField entityField : model.getFields()) {
            if (!entityField.isEditable()) {
                continue;
            }
            addFormField(createFormField(entityField));
        }

        buildLayout();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    private FormField createFormField(EntityField entityField) {
        FormField formField;
        switch (entityField.getType()) {
            case String:
                formField = new StringFormField(entityField.getTitleKey());
                break;
            case Integer:
                formField = new IntegerFormField(entityField.getTitleKey());
                break;
            case Double:
                formField = new DoubleFormField(entityField.getTitleKey());
                break;
            case Boolean:
                formField = new BooleanFormField(entityField.getTitleKey());
                break;
            case Date:
                formField = new DateFormField(entityField.getTitleKey());
                break;
            default:
                formField =  entityField.getType().isEntityClass() ? new EntityFormField(entityField , this.entity)
                        : new StringFormField(entityField.getTitleKey());
        }
        entityFormFieldMap.put(entityField, formField);
        return formField;
    }

    protected FormField createFormField(EntityField entityField, Entity entity) {
        FormField field = createFormField(entityField);
        if (entity != null) {
            field.setValue(entityField.get(entity));
        }
        return field;
    }

    /**
     * Assembles values of the input fields into a Entity object.
     */
    protected Entity buildEntity() {
        Entity entity = this.entity;
        if (entity == null) {
            entity = createNewEntity();
        }

        for (Map.Entry<EntityField, FormField> entry : this.entityFormFieldMap.entrySet()) {
            entry.getKey().set(entity, entry.getValue().getValue());
        }

        if (this.innerTableView != null) {
            this.innerTableView.getFullEntityField().set(entity, this.innerTableView.getEntityList());
        }

        return entity;
    }

    /**
     * Return a new empty Entity instance.
     * Default implementation based on EntityModel::createNewEntity
     * Override this method to change the returned Entity type
     * @return
     */
    protected Entity createNewEntity() {
        return this.model.createNewEntity();
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void setCancelActionListener(ViewActionListener cancelActionListener) {
        this.cancelActionListener = cancelActionListener;
    }

    public void setChangeActionListener(ViewActionListener changeActionListener) {
        this.changeActionListener = changeActionListener;
    }

    protected void setInnerTable(EntityInnerTableView innerTableView) {
        this.innerTableView = innerTableView;
        getContentPanel().add(innerTableView.getContentPanel(), BorderLayout.CENTER);
    }
}
