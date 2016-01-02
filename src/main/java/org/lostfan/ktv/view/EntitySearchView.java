package org.lostfan.ktv.view;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;

import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.*;
import org.lostfan.ktv.view.components.DatePickerField;
import org.lostfan.ktv.view.components.IntegerTextField;
import org.lostfan.ktv.view.model.CriteriaComboBoxModel;
import org.lostfan.ktv.view.model.FieldsComboBoxModel;

public class EntitySearchView extends FrameView {

    private class ModelObserver implements org.lostfan.ktv.utils.Observer {
        @Override
        public void update(Object args) {
            EntitySearchView.this.revalidate();
        }
    }

    private class CriterionComponents {

        private JComboBox<String> fieldComboBox;
        private JComboBox<String> criterionComboBox;
        private IntegerTextField integerTextField;
        private JTextField valueTextField;
        private DatePickerField datePicker;
        private JButton removeButton;

        public CriterionComponents() {
            this.fieldComboBox = new JComboBox<String>(new FieldsComboBoxModel(model.getFields()));

            this.fieldComboBox.addActionListener(e -> {
                criterionComboBox = new JComboBox<>(
                        new CriteriaComboBoxModel(SearchCriteria.getCritera(getSelectedFieldType())));
                EntitySearchView.this.rebuildCriteriaPanel();
            });

            this.criterionComboBox = new JComboBox<>();
            this.valueTextField = new JTextField(20);
            this.integerTextField = new IntegerTextField();
            this.datePicker = new DatePickerField();
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

        public EntityFieldTypes getSelectedFieldType() {
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
                    return this.integerTextField.getValue();
                case Boolean:
                    return getSelectedCriterion() == SearchCriteria.Boolean.True;
                case Date:
                    return this.datePicker.getValue();
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
            if (getSelectedFieldType() != null && getSelectedFieldType() != EntityFieldTypes.Boolean && getSelectedFieldType() != EntityFieldTypes.Date  && getSelectedFieldType() != EntityFieldTypes.Integer) {
                panel.add(this.valueTextField, c);
            }
            if (getSelectedFieldType() != null && getSelectedFieldType() == EntityFieldTypes.Integer) {
                panel.add(this.integerTextField, c);
            }
            if (getSelectedFieldType() != null && getSelectedFieldType() == EntityFieldTypes.Date) {
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

    private JPanel criteriaPanel;
    private List<CriterionComponents> criteria;
    private JButton addButton;
    private JButton findButton;
    private JButton cancelButton;
    private EntityModel model;

    private ViewActionListener findActionListener;

    private ModelObserver modelObserver;

    public EntitySearchView(EntityModel model) {
        this.model = model;
        setTitle(getGuiString("buttons.search") + ": " + getEntityString(model.getEntityNameKey()));

        this.criteria = new ArrayList<>();

        this.addButton = new JButton(getGuiString("buttons.addCriterion"));
        this.addButton.setFocusable(false);
        this.addButton.addActionListener(e -> {
            criteria.add(new CriterionComponents());
            rebuildCriteriaPanel();
        });

        this.findButton = new JButton(getGuiString("buttons.find"));
        this.findButton.addActionListener(e -> {
            if (this.findActionListener != null) {
                this.findActionListener.actionPerformed(null);
                hide();
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            hide();
        });

        buildLayout();
    }

    private void buildLayout() {
        setSize(WIDTH, HEIGHT);

        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        this.criteriaPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(this.criteriaPanel);
        getContentPanel().add(scrollPane, BorderLayout.CENTER);

        JPanel butPanel = new JPanel();
        butPanel.add(findButton);
        butPanel.add(cancelButton);
        butPanel.add(addButton);
        getContentPanel().add(butPanel, BorderLayout.SOUTH);

        rebuildCriteriaPanel();

        this.modelObserver = new ModelObserver();

        model.addObserver(this.modelObserver);
    }

    private void rebuildCriteriaPanel() {

        this.criteriaPanel.removeAll();

        for (int i = 0; i < this.criteria.size(); i++) {
            this.criteria.get(i).addComponentsTo(this.criteriaPanel, i + 1);
        }

        revalidate();
    }

    public void setFindActionListener(ViewActionListener findActionListener) {
        this.findActionListener = findActionListener;
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

