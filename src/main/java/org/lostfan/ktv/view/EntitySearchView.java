package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;

import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.model.*;

public class EntitySearchView {
    
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "dd-MM-yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }

    private class CriterionComponents {

        private JComboBox<String> fieldComboBox;
        private JComboBox<String> criterionComboBox;
        private JTextField valueTextField;
        private JDatePickerImpl datePicker;
        private JButton removeButton;

        public CriterionComponents() {

            this.fieldComboBox = new JComboBox<String>(model.getFieldComboBoxModel());

            this.fieldComboBox.addActionListener(e -> {
                criterionComboBox = new JComboBox<>(
                        new CriteriaComboBoxModel(SearchCriteria.getCritera(getSelectedFieldType())));
                EntitySearchView.this.rebuildCriteriaPanel();
            });

            this.criterionComboBox = new JComboBox<>();
            this.valueTextField = new JTextField(20);
            this.datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()), new DateLabelFormatter());
            this.removeButton = new JButton();
            URL url = EntitySearchView.class.getClassLoader().getResource("images/remove.png");
            if(url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image image = icon.getImage().getScaledInstance(10,10,Image.SCALE_SMOOTH);
                icon = new ImageIcon(image);
                this.removeButton.setIcon(icon);
            }

            this.removeButton.addActionListener(e -> {
                EntitySearchView.this.criteria.remove(CriterionComponents.this);
                EntitySearchView.this.rebuildCriteriaPanel();
            });
        }

        public EntityField.Types getSelectedFieldType() {
            if (fieldComboBox.getSelectedItem() == null) {
                return null;
            }
            return ((FieldsComboBoxModel) fieldComboBox.getModel()).getSelectedField().getType();
        }

        public EntityField getSelectedField() {
            return ((FieldsComboBoxModel) fieldComboBox.getModel()).getSelectedField();
        }

        public SearchCriteria getSelectedCriterion() {
            return ((CriteriaComboBoxModel) criterionComboBox.getModel()).getSelectedCriterion();
        }

        public Object getValue() {
            switch (getSelectedFieldType()) {
                case String:
                    return this.valueTextField.getText();
                case Integer:
                    return Integer.parseInt(this.valueTextField.getText());
                case Boolean:
                    return getSelectedCriterion() == SearchCriteria.Boolean.True;
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
            panel.add(this.fieldComboBox, c);
            if (this.fieldComboBox.getSelectedItem() != null) {
                panel.add(this.criterionComboBox, c);
            }
            if (getSelectedFieldType() != null && getSelectedFieldType() != EntityField.Types.Boolean && getSelectedFieldType() != EntityField.Types.Date) {
                panel.add(this.valueTextField, c);
            }
            if (getSelectedFieldType() != null && getSelectedFieldType() == EntityField.Types.Date) {
                panel.add(this.datePicker, c);
            }


            panel.add(this.removeButton, c);

            c.gridy = criteriaNumber;
            c.anchor = GridBagConstraints.NORTH;

            rootPanel.add(panel, c);
        }
    }

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    private JFrame frame;
    private JPanel criteriaPanel;
    private List<CriterionComponents> criteria;
    private JButton addButton;
    private JButton findButton;
    private JButton cancelButton;
    private Model model;

    public EntitySearchView(Model model) {
        this.model = model;
        this.frame = new JFrame("Поиск: " + model.getEntityName());
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.criteria = new ArrayList<>();

        this.addButton = new JButton("Добавить условие");
        this.addButton.setFocusable(false);
        this.addButton.addActionListener(e -> {
            criteria.add(new CriterionComponents());
            rebuildCriteriaPanel();
        });

        this.findButton = new JButton("Найти");

        this.cancelButton = new JButton("Отмена");
        this.cancelButton.addActionListener(e -> {
            frame.setVisible(false);
        });

        buildLayout();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void buildLayout() {
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        frame.setLayout(new BorderLayout());


        this.criteriaPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(this.criteriaPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel butPanel = new JPanel();
        butPanel.add(findButton);
        butPanel.add(cancelButton);
        butPanel.add(addButton);
        frame.add(butPanel, BorderLayout.SOUTH);

        rebuildCriteriaPanel();
    }

    private void rebuildCriteriaPanel() {

        this.criteriaPanel.removeAll();

        for (int i = 0; i < this.criteria.size(); i++) {
            this.criteria.get(i).addComponentsTo(this.criteriaPanel, i + 1);
        }

        this.frame.revalidate();
        this.frame.repaint();
    }

    public void addFindActionListener(ActionListener listener) {
        this.findButton.addActionListener(listener);
    }

    public List<FieldSearchCriterion> getSearchCriteria() {
        List<FieldSearchCriterion> fieldCriteria = new ArrayList<>(this.criteria.size());
        for (CriterionComponents criterionComponents : this.criteria) {
            EntityField selectedField = criterionComponents.getSelectedField();
            SearchCriteria selectedCriterion = criterionComponents.getSelectedCriterion();
            if (selectedField == null || selectedCriterion == null) {
                continue;
            }
            FieldSearchCriterion fieldSearchCriterion =
                    new FieldSearchCriterion(selectedField, selectedCriterion, criterionComponents.getValue());
            fieldCriteria.add(fieldSearchCriterion);
        }

        return fieldCriteria;
    }

}
