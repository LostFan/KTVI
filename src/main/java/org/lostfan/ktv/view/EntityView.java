package org.lostfan.ktv.view;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.controller.ComboBoxController;
import org.lostfan.ktv.model.EntityComboBoxModel;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.utils.DateLabelFormatter;
import org.lostfan.ktv.utils.DefaultContextMenu;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

/**
 * Created by 1 on 08.10.2015.
 */
public class EntityView {

    private class NameAndValueField {

        private JLabel label;
        private JComponent jComponent;
        private EntityField entityField;

        public NameAndValueField(EntityField entityField) {
            this.entityField = entityField;
            switch (entityField.getType()) {
                case String:
                case Integer:
                    this.jComponent = new JTextField(20);
                    break;
                case Boolean:
                    this.jComponent = new JCheckBox();
                    break;
                case Date:
                    this.jComponent = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()), new DateLabelFormatter());
                    break;
                default:
                    this.jComponent = new JComboBox();
                    ((JComboBox) this.jComponent).setEditable(true);
            }

            this.label = new JLabel(ResourceBundles.getEntityBundle().getString(entityField.getTitleKey()), SwingConstants.LEFT);

        }

        public EntityField.Types getSelectedFieldType() {
            return entityField.getType();
        }


        public Object getValue() {
            switch (getSelectedFieldType()) {
                case String:
                    return ((JTextField) this.jComponent).getText();
                case Integer:
                    return !((JTextField) this.jComponent).getText().isEmpty() ? Integer.parseInt(((JTextField) this.jComponent).getText()): null;
                case Boolean:
                    return ((JCheckBox) this.jComponent).isSelected();
                case Date:
                    java.sql.Date selectedDate = new java.sql.Date(((Date) ((JDatePickerImpl) this.jComponent).getModel().getValue()).getTime());
                    return selectedDate.toLocalDate();
                default:
                    return  ((ComboBoxView) this.jComponent).getSelectedId();
            }
//            return null;
        }

        public void addComponentsTo(JPanel rootPanel, int criteriaNumber) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.insets = new Insets(0,10,10,10);
            panel.add(this.label, c);
            DefaultContextMenu contextMenu = new DefaultContextMenu();
            if (getSelectedFieldType() == EntityField.Types.String || getSelectedFieldType() == EntityField.Types.Integer) {
                contextMenu.add((JTextField) this.jComponent);
            }
//            if (getSelectedFieldType() == EntityField.Types.Date) {
//                panel.add(this.jComponent, c);
//            }
//            if (getSelectedFieldType() == EntityField.Types.Boolean) {
//                panel.add(this.jComponent, c);
//            }

            if(getSelectedFieldType().isEntityClass()) {

                List<EntityComboBoxModel> entityComboBoxModels= model.getEntityComboBoxModels();
                for (EntityComboBoxModel entityComboBoxModel : entityComboBoxModels) {
                    if(entityComboBoxModel.getEntityClass() == getSelectedFieldType().getClazz()) {
                        this.jComponent = new ComboBoxView(entityComboBoxModel);
                        contextMenu.add((JTextField) ((JComboBox)this.jComponent).getEditor().getEditorComponent());
//                        new ComboBoxController(entityComboBoxModel, comboBoxView);
//                        this.jComponent = comboBoxView.getJComboBox();
                    }
                }



            }


            panel.add(this.jComponent, c);
            c.gridy = criteriaNumber;
            c.anchor = GridBagConstraints.NORTH;

            rootPanel.add(panel, c);
        }

        public EntityField getEntityField() {
            return entityField;
        }
    }
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private JFrame frame;
    private List<NameAndValueField> nameAndValueFields;
    private JButton addButton;
    private JButton cancelButton;
    private EntityModel model;

    public EntityView(EntityModel model) {
        nameAndValueFields = new ArrayList<>();
        this.model = model;
        this.frame = new JFrame(ResourceBundles.getEntityBundle().getString(model.getEntityNameKey()));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addButton = new JButton(getString("buttons.add"));
        this.addButton.addActionListener(e -> {
            frame.setVisible(false);
        });
        this.cancelButton = new JButton(getString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            frame.setVisible(false);
        });

        for (Object entityField : model.getFields()) {
            nameAndValueFields.add(new NameAndValueField((EntityField) entityField));
        }
        buildLayout();
        frame.setVisible(true);
    }

    public EntityView(EntityModel model, Object entity) {

        this(model);

        this.addButton.setText(getString("buttons.change"));
        for (NameAndValueField nameAndValueField : nameAndValueFields) {
            Object o = nameAndValueField.getEntityField().get(entity);
            if (nameAndValueField.getSelectedFieldType() == EntityField.Types.String ) {
                ((JTextField) nameAndValueField.jComponent).setText(o.toString());
            } else
            if (nameAndValueField.getSelectedFieldType() == EntityField.Types.Integer) {
                if(o != null) {
                    ((JTextField) nameAndValueField.jComponent).setText(o.toString());
                } else {
                    ((JTextField) nameAndValueField.jComponent).setText("0");
                }
            } else
            if (nameAndValueField.getSelectedFieldType() == EntityField.Types.Date) {
                LocalDate localDate = (LocalDate) o;
                ((JDatePickerImpl) nameAndValueField.jComponent).getModel().setDate(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
                ((JDatePickerImpl) nameAndValueField.jComponent).getModel().setSelected(true);
            } else
            if (nameAndValueField.getSelectedFieldType() == EntityField.Types.Boolean) {

                ((JCheckBox) nameAndValueField.jComponent).setSelected((Boolean) o);
            } else {
                ((ComboBoxView) nameAndValueField.jComponent).setId((Integer) o);
                o =((ComboBoxView) nameAndValueField.jComponent).getSelectedName();
//                ((ComboBoxView) nameAndValueField.jComponent).setSelectedItem(o);
                ((JTextField)(((ComboBoxView) nameAndValueField.jComponent).getEditor().getEditorComponent())).setText((String)o);
            }
        }

        frame.revalidate();
        frame.repaint();
    }

    private void buildLayout() {

        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        frame.add(panel, BorderLayout.LINE_START);

        JPanel panelInner = new JPanel(new GridLayout(4, 1, 0, 10));
        panel.add(panelInner);

        for (int i = 0; i < this.nameAndValueFields.size(); i++) {
            this.nameAndValueFields.get(i).addComponentsTo(panelInner, i + 1);
        }
        JPanel butPanel = new JPanel();
        butPanel.add(addButton);
        butPanel.add(cancelButton);
        frame.add(butPanel, BorderLayout.SOUTH);

    }

    public Map<String, Object> getValues() {
        Map<String, Object> map = new HashMap<>();
        for (NameAndValueField nameAndValueField : this.nameAndValueFields) {
            map.put(nameAndValueField.getEntityField().getTitleKey(), nameAndValueField.getValue());
//            String fieldName = nameAndValueFields.getEntityField().getTitleKey();
//            FieldValue fieldValue =
//                    new FieldValue(fieldName,  nameAndValueFields.getValue());
//            fieldValues.add(fieldValue);
        }

        return map;
    }

    public void addAddActionListener(ActionListener listener) {
        this.addButton.addActionListener(listener);
    }

    public void addCancelActionListener(ActionListener listener) {
        this.addButton.addActionListener(listener);
    }

    private String getString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
