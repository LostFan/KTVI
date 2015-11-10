package org.lostfan.ktv.view;

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

/**
 * Created by 1 on 08.10.2015.
 */
public class EntityView {

    private class LabelFieldPanel extends JPanel {

        private JLabel label;
        private JComponent jComponent;
        private EntityField entityField;
        private final static int COMBOBOX_INNER_POSITION = 0;
        private JPanel jPanel = new JPanel(new BorderLayout());

        public LabelFieldPanel(EntityField entityField) {
            this.entityField = entityField;
            switch (entityField.getType()) {
                case String:
                case Integer:
                case Double:
                    JTextField textField = new JTextField(20);
                    if (EntityView.this.entity != null) {
                        textField.setText(String.valueOf(entityField.get(EntityView.this.entity)));
                    }
                    this.jComponent = textField;
                    break;
                case Boolean:
                    JCheckBox checkBox = new JCheckBox();
                    if (EntityView.this.entity != null) {
                        checkBox.setSelected((Boolean)entityField.get(EntityView.this.entity));
                    }
                    this.jComponent = checkBox;
                    break;
                case Date:
                    JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()), new DateLabelFormatter());
                    if (EntityView.this.entity != null) {
                        LocalDate localDate = (LocalDate) entityField.get(EntityView.this.entity);
                        datePicker.getModel().setDate(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
                        datePicker.getModel().setSelected(true);
                    }
                    this.jComponent = datePicker;
                    break;
                default:
                    EntityComboBox comboBox = EntityComboBoxFactory.createComboBox(entityField.getType());
                    comboBox.setEditable(true);
                    if (EntityView.this.entity != null) {
                        Object value = entityField.get(EntityView.this.entity);
                        comboBox.setSelectedId((Integer) value);
                        value = comboBox.getSelectedName();
                        ((JTextField)((comboBox).getEditor().getEditorComponent())).setText((String) value);
                    }
                    jPanel.add(comboBox, BorderLayout.WEST);
                    JButton tableButton = new JButton("...");
                    tableButton.setPreferredSize(new Dimension(20, 10));
                    tableButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            EntitySelectionView entitySelectionView = EntitySelectionFactory.createForm(entityField.getType());
                            if (entitySelectionView.getSelectedEntity() != null) {
                                comboBox.setSelectedId(entitySelectionView.getSelectedEntity().getId());
                                ((JTextField) ((comboBox).getEditor().getEditorComponent())).setText(entitySelectionView.getSelectedEntity().getName());
                                EntityView.this.frame.invalidate();
                                EntityView.this.frame.repaint();
                            }
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

                    entityButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            EntityModel entityModel = EntityModelFactory.createForm(entityField.getType());
                            EntityView entityView = new EntityView(entityModel, entityModel.getEntity(comboBox.getSelectedId()));
                            EntityOneController entityOneController = new EntityOneController(entityModel, entityView);
                            entityView.changeActionListener.actionPerformed(null);
                        }
                    });

                    jPanel.add(entityButton, BorderLayout.EAST);
                    this.jComponent = jPanel;
            }

            this.label = new JLabel(ResourceBundles.getEntityBundle().getString(entityField.getTitleKey()), SwingConstants.LEFT);

            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.insets = new Insets(0, 10, 10, 10);
            add(this.label, c);
            DefaultContextMenu contextMenu = new DefaultContextMenu();
            if (this.entityField.getType() == EntityFieldTypes.String
                    || this.entityField.getType() == EntityFieldTypes.Integer
                    || this.entityField.getType() == EntityFieldTypes.Double) {
                contextMenu.add((JTextField) this.jComponent);
            }

            if(this.entityField.getType().isEntityClass()) {
                contextMenu.add((JTextField) ((JComboBox)this.jComponent.getComponent(COMBOBOX_INNER_POSITION)).getEditor().getEditorComponent());
            }
            add(this.jComponent, c);
        }


        public Object getValue() {
            switch (this.entityField.getType()) {
                case String:
                    return ((JTextField) this.jComponent).getText();
                case Integer:
                    return !((JTextField) this.jComponent).getText().isEmpty() ? Integer.parseInt(((JTextField) this.jComponent).getText()): null;
                case Double:
                    return !((JTextField) this.jComponent).getText().isEmpty() ? Double.parseDouble(((JTextField) this.jComponent).getText()): null;
                case Boolean:
                    return ((JCheckBox) this.jComponent).isSelected();
                case Date:
                    java.sql.Date selectedDate = new java.sql.Date(((Date) ((JDatePickerImpl) this.jComponent).getModel().getValue()).getTime());
                    return selectedDate.toLocalDate();
                default:
                    return  ((EntityComboBox) this.jComponent.getComponent(COMBOBOX_INNER_POSITION)).getSelectedId();
            }
        }

        public EntityField getEntityField() {
            return this.entityField;
        }
    }

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private JFrame frame;
    private List<LabelFieldPanel> labelFieldPanels;
    private JButton addButton;
    private JButton cancelButton;
    private EntityModel model;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;
    private ViewActionListener changeActionListener;

    private Entity entity;

    public EntityView(EntityModel model) {
        this(model, null);
    }

    public EntityView(EntityModel model, Entity entity) {
        this.entity = entity;
        this.model = model;
        this.frame = new JFrame(ResourceBundles.getEntityBundle().getString(model.getEntityNameKey()));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        });

        labelFieldPanels = new ArrayList<>();
        for (Object entityField : model.getFields()) {
            labelFieldPanels.add(new LabelFieldPanel((EntityField) entityField));
        }
        buildLayout();
        frame.setVisible(true);
    }

    private void buildLayout() {

        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panel = new JPanel(new GridLayout(2,1));
        frame.add(panel, BorderLayout.LINE_START);

        JPanel panelInner = new JPanel(new GridLayout(4, 1, 0, 10));
        panel.add(panelInner);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.NORTH;
        for (int i = 0; i < this.labelFieldPanels.size(); i++) {
            c.gridy = i;
            panelInner.add(this.labelFieldPanels.get(i), c);
        }

        JPanel butPanel1 = new JPanel();
        List<EntityModel> entityModels = model.getTableModels();
        if(entityModels != null) {
            for (EntityModel entityModel : entityModels) {
                EntityInnerTableView entityInnerTableView =
                        new EntityInnerTableView(entityModel, this.entity == null ? null : this.entity.getId());
                butPanel1.add(entityInnerTableView.getContentPanel());
            }
        }
        panel.add(butPanel1);
        JPanel butPanel = new JPanel();
        butPanel.add(addButton);
        butPanel.add(cancelButton);
        frame.add(butPanel, BorderLayout.SOUTH);

    }

    public Entity getEntity() {
        Entity entity = this.entity;
        if (entity == null) {
            entity = this.model.createNewEntity();
        }

        for (LabelFieldPanel labelFieldPanel : this.labelFieldPanels) {
            labelFieldPanel.getEntityField().set(entity, labelFieldPanel.getValue());
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
