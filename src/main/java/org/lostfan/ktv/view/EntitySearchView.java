package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.utils.DateLabelFormatter;
import org.lostfan.ktv.utils.ResourceBundles;

public class EntitySearchView {

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
                case Service:
                case Subscriber:
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
//            panel.add(new JList<>());
//            panel.add(new JPopupMenu());


            panel.add(this.removeButton, c);

            c.gridy = criteriaNumber;
            c.anchor = GridBagConstraints.NORTH;

            rootPanel.add(panel, c);
        }
    }

    public static final int WIDTH = 750;
    public static final int HEIGHT = 500;

    private JDialog frame;
//    private JDialog dialog;
    private JPanel criteriaPanel;
    private List<CriterionComponents> criteria;
    private JButton addButton;
    private JButton findButton;
    private JButton cancelButton;
    private EntityModel model;

    public EntitySearchView(EntityModel model) {
        this.model = model;
        this.frame = new JDialog(new JFrame(), getString("buttons.search") + ": " +
                ResourceBundles.getEntityBundle().getString(model.getEntityNameKey()), true);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.criteria = new ArrayList<>();

        this.addButton = new JButton(getString("buttons.addCriterion"));
        this.addButton.setFocusable(false);
        this.addButton.addActionListener(e -> {
            criteria.add(new CriterionComponents());
            rebuildCriteriaPanel();
        });

        this.findButton = new JButton(getString("buttons.find"));
        this.findButton.addActionListener(e -> {
            frame.setVisible(false);
        });

        this.cancelButton = new JButton(getString("buttons.cancel"));
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

    private String getString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}

