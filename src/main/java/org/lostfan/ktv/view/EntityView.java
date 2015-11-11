package org.lostfan.ktv.view;

import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.controller.EntityOneController;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.*;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.components.EntityComboBox;
import org.lostfan.ktv.view.components.EntityComboBoxFactory;
import org.lostfan.ktv.view.components.EntityModelFactory;
import org.lostfan.ktv.view.components.EntitySelectionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by 1 on 08.10.2015.
 */
public class EntityView {

    private class LabelFieldPanel {

        private JLabel label;
        /**
         * A Component that represents the source to input a value
         */
        private JComponent inputComponent;
        private EntityField entityField;
        /**
         * Represents a supplier to retrieve the input value.
         */
        private Supplier<Object> valueSupplier;

        public LabelFieldPanel(EntityField entityField) {
            this.entityField = entityField;
            switch (entityField.getType()) {
                case String:
                case Integer:
                case Double:
                    JTextField textField = new JTextField(20);
                    textField.setMargin(new Insets(2, 6, 2, 6));
                    if (EntityView.this.entity != null) {
                        textField.setText(String.valueOf(entityField.get(EntityView.this.entity)));
                    }
                    new DefaultContextMenu().add(textField);
                    this.inputComponent = textField;
                    // TODO: textField with a Formatter to filter inappropriate values
                    switch (entityField.getType()) {
                        case String:
                            this.valueSupplier = textField::getText;
                            break;
                        case Integer:
                            this.valueSupplier = () -> {
                                String text = textField.getText();
                                if (text == null) {
                                    return null;
                                }
                                try {
                                    return Integer.parseInt(text);
                                } catch (NumberFormatException e) {
                                    return null;
                                }
                            };
                            break;
                        case Double:
                            this.valueSupplier = () -> {
                                String text = textField.getText();
                                try {
                                    return Double.parseDouble(text);
                                } catch (NumberFormatException | NullPointerException e) {
                                    return null;
                                }
                            };
                            break;
                    }
                    break;
                case Boolean:
                    JCheckBox checkBox = new JCheckBox();
                    if (EntityView.this.entity != null) {
                        checkBox.setSelected((Boolean)entityField.get(EntityView.this.entity));
                    }
                    this.inputComponent = checkBox;
                    this.valueSupplier = checkBox::isSelected;
                    break;
                case Date:
                    JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()), new DateLabelFormatter());
                    if (EntityView.this.entity != null) {
                        LocalDate localDate = (LocalDate) entityField.get(EntityView.this.entity);
                        datePicker.getModel().setDate(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
                        datePicker.getModel().setSelected(true);
                    }
                    this.inputComponent = datePicker;
                    this.valueSupplier = () -> new java.sql.Date(((Date)datePicker.getModel().getValue()).getTime()).toLocalDate();
                    break;
                default:
                    EntityComboBox comboBox = EntityComboBoxFactory.createComboBox(entityField.getType());
                    new DefaultContextMenu().add((JTextField) comboBox.getEditor().getEditorComponent());
                    comboBox.setEditable(true);
                    if (EntityView.this.entity != null) {
                        Object value = entityField.get(EntityView.this.entity);
                        comboBox.setSelectedId((Integer) value);
                        value = comboBox.getSelectedName();
                        ((JTextField)((comboBox).getEditor().getEditorComponent())).setText((String) value);
                    }
                    JPanel jPanel = new JPanel(new BorderLayout());
                    jPanel.add(comboBox, BorderLayout.WEST);

                    // TODO: Move the following 2 buttons into the EntityComboBox component
                    // and convert it to a JPanel that contains all these 3 components
                    JButton tableButton = new JButton("...");
                    tableButton.setPreferredSize(new Dimension(20, 10));
                    tableButton.addActionListener(e -> {
                        EntitySelectionView entitySelectionView = EntitySelectionFactory.createForm(entityField.getType());
                        if (entitySelectionView.getSelectedEntity() != null) {
                            comboBox.setSelectedId(entitySelectionView.getSelectedEntity().getId());
                            ((JTextField) ((comboBox).getEditor().getEditorComponent())).setText(entitySelectionView.getSelectedEntity().getName());
                            EntityView.this.frame.invalidate();
                            EntityView.this.frame.repaint();
                        }
                    });
                    jPanel.add(tableButton, BorderLayout.CENTER);

                    JButton entityButton = new JButton();
                    entityButton.setPreferredSize(new Dimension(20, 10));
                    URL url = EntitySearchView.class.getClassLoader().getResource("images/search.png");
                    if(url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        Image image = icon.getImage().getScaledInstance(10,10,Image.SCALE_SMOOTH);
                        icon = new ImageIcon(image);
                        entityButton.setIcon(icon);
                    }

                    entityButton.addActionListener(e -> {
                        EntityModel entityModel = EntityModelFactory.createForm(entityField.getType());
                        entityModel.addObserver(comboBox.getModelObserver());
                        EntityView entityView = new EntityView(entityModel, entityModel.getEntity(comboBox.getSelectedId()));
                        EntityOneController entityOneController = new EntityOneController(entityModel, entityView);
                        entityView.changeActionListener.actionPerformed(null);
                    });

                    jPanel.add(entityButton, BorderLayout.EAST);
                    this.inputComponent = jPanel;
                    this.valueSupplier = comboBox::getSelectedId;
            }

            this.label = new JLabel(ResourceBundles.getEntityBundle().getString(entityField.getTitleKey()), SwingConstants.LEFT);
            this.label.setLabelFor(this.inputComponent);
        }
    }

    public static final int WIDTH = 450;
    public static final int HEIGHT = 550;

    private JFrame frame;
    private List<LabelFieldPanel> labelFieldPanels;
    private JButton addButton;
    private JButton cancelButton;
    private EntityModel<? extends Entity> model;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;
    private ViewActionListener changeActionListener;

    private Entity entity;

    public EntityView(EntityModel model) {
        this(model, null);
    }

    public <E extends Entity> EntityView(EntityModel<E> model, Entity entity) {
        this.entity = entity;
        this.model = model;

        this.frame = new JFrame(ResourceBundles.getEntityBundle().getString(model.getEntityNameKey()));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setLocationRelativeTo(null);

        this.addButton = new JButton(getString(entity == null ? "buttons.add" : "buttons.change"));
        this.addButton.addActionListener(e -> {
            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(null);
            }
        });

        this.cancelButton = new JButton(getString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            if (this.cancelActionListener != null) {
                this.cancelActionListener.actionPerformed(null);
            }
            hide();
        });

        labelFieldPanels = new ArrayList<>();
        for (EntityField entityField : model.getFields()) {
            if (!entityField.isEditable()) {
                continue;
            }
            labelFieldPanels.add(new LabelFieldPanel(entityField));
        }

        buildLayout();

        frame.setVisible(true);
    }

    private void buildLayout() {

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        frame.add(contentPanel, BorderLayout.PAGE_START);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < this.labelFieldPanels.size(); i++) {
            c.gridy = i;
            c.gridx = 0;
            contentPanel.add(this.labelFieldPanels.get(i).label, c);
            c.gridx = 1;
            contentPanel.add(this.labelFieldPanels.get(i).inputComponent, c);
        }

        List<EntityModel> entityModels = model.getTableModels();
        if(entityModels != null) {
            for (EntityModel entityModel : entityModels) {
                EntityInnerTableView entityInnerTableView =
                        new EntityInnerTableView(entityModel, this.entity == null ? null : this.entity.getId());
                frame.add(entityInnerTableView.getContentPanel(), BorderLayout.CENTER);
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    public Entity getEntity() {
        Entity entity = this.entity;
        if (entity == null) {
            entity = this.model.createNewEntity();
        }

        for (LabelFieldPanel labelFieldPanel : this.labelFieldPanels) {
            labelFieldPanel.entityField.set(entity, labelFieldPanel.valueSupplier.get());
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
