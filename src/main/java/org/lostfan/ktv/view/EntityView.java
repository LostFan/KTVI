package org.lostfan.ktv.view;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.controller.ComboBoxController;
import org.lostfan.ktv.model.EntityComboBoxModel;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.utils.DateLabelFormatter;
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

    private class NameAndValueFields {

        private JLabel label;
        private JTextField valueTextField;
        private JCheckBox checkBox;
        private JDatePickerImpl datePicker;
        private EntityField entityField;
        private JComboBox jComboBox;

        public NameAndValueFields(EntityField entityField) {
            this.entityField = entityField;
            this.label = new JLabel(ResourceBundles.getEntityBundle().getString(entityField.getTitleKey()), SwingConstants.LEFT);
            this.checkBox = new JCheckBox();
            this.valueTextField = new JTextField(20);
            this.datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()), new DateLabelFormatter());
            this.jComboBox = new JComboBox();
            this.jComboBox.setEditable(true);
        }

        public EntityField.Types getSelectedFieldType() {
            return entityField.getType();
        }


        public Object getValue() {
            switch (getSelectedFieldType()) {
                case String:
                    return this.valueTextField.getText();
                case Integer:
                    return !this.valueTextField.getText().isEmpty() ? Integer.parseInt(this.valueTextField.getText()): null;
                case Boolean:
                    return this.checkBox.isSelected();
                case Date:
                    java.sql.Date selectedDate = new java.sql.Date(((Date) datePicker.getModel().getValue()).getTime());
                    return selectedDate.toLocalDate();
            }
            return null;
        }

        public void addComponentsTo(JPanel rootPanel, int criteriaNumber) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.insets = new Insets(0,10,10,10);
            panel.add(this.label, c);
            if (getSelectedFieldType() == EntityField.Types.String || getSelectedFieldType() == EntityField.Types.Integer) {
                panel.add(this.valueTextField, c);
            }
            if (getSelectedFieldType() == EntityField.Types.Date) {
                panel.add(this.datePicker, c);
            }
            if (getSelectedFieldType() == EntityField.Types.Boolean) {
                panel.add(this.checkBox, c);
            }

            if(getSelectedFieldType().isEntityClass()) {

                List<EntityComboBoxModel> entityComboBoxModels= model.getEntityComboBoxModels();
                for (EntityComboBoxModel entityComboBoxModel : entityComboBoxModels) {
                    if(entityComboBoxModel.getEntityClass() == getSelectedFieldType().getClazz()) {
                        ComboBoxView comboBoxView = new ComboBoxView(entityComboBoxModel);
                        new ComboBoxController(entityComboBoxModel, comboBoxView);
                        jComboBox = comboBoxView.getJComboBox();
                    }
                }


                panel.add(this.jComboBox, c);
            }

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
    private List<NameAndValueFields> nameAndValueFieldses;
    private JButton addButton;
    private JButton cancelButton;
    private EntityModel model;

    public EntityView(EntityModel model) {
        nameAndValueFieldses = new ArrayList<>();
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
            nameAndValueFieldses.add(new NameAndValueFields((EntityField) entityField));
        }
        buildLayout();
        frame.setVisible(true);
    }

    public EntityView(EntityModel model, Object entity) {

        this(model);

        this.addButton.setText(getString("buttons.change"));

        for (NameAndValueFields nameAndValueFields : nameAndValueFieldses) {
            Object o = nameAndValueFields.getEntityField().get(entity);
            if (nameAndValueFields.getSelectedFieldType() == EntityField.Types.String ) {
                nameAndValueFields.valueTextField.setText(o.toString());
            } else
            if (nameAndValueFields.getSelectedFieldType() == EntityField.Types.Integer) {
                if(o != null) {
                    nameAndValueFields.valueTextField.setText(o.toString());
                } else {
                    nameAndValueFields.valueTextField.setText("0");
                }
            } else
            if (nameAndValueFields.getSelectedFieldType() == EntityField.Types.Date) {
                LocalDate localDate = (LocalDate) o;
                nameAndValueFields.datePicker.getModel().setDate(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
                nameAndValueFields.datePicker.getModel().setSelected(true);
            } else
            if (nameAndValueFields.getSelectedFieldType() == EntityField.Types.Boolean) {

                nameAndValueFields.checkBox.setSelected((Boolean) o);
            } else {
                nameAndValueFields.jComboBox.setSelectedItem(o);
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

        for (int i = 0; i < this.nameAndValueFieldses.size(); i++) {
            this.nameAndValueFieldses.get(i).addComponentsTo(panelInner, i + 1);
        }
        JPanel butPanel = new JPanel();
        butPanel.add(addButton);
        butPanel.add(cancelButton);
        frame.add(butPanel, BorderLayout.SOUTH);

    }

    public Map<String, Object> getValues() {
        Map<String, Object> map = new HashMap<>();
        for (NameAndValueFields nameAndValueFields : this.nameAndValueFieldses) {
            map.put(nameAndValueFields.getEntityField().getTitleKey(), nameAndValueFields.getValue());
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
