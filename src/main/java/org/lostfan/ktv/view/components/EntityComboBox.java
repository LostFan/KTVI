package org.lostfan.ktv.view.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.utils.Observer;
import org.lostfan.ktv.utils.ViewActionListener;

public class EntityComboBox extends JComboBox<String> {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntityComboBox.this.revalidateResult();
        }
    }

    private JTextField textField;
    private EntitySearcherModel model;
    private EntityComboBoxModel entityComboBoxModel;
    private ViewActionListener searchActionListener;

    private ModelObserver modelObserver;

    public EntityComboBox(EntitySearcherModel model) {
        textField = (JTextField)     this.getEditor().getEditorComponent();
        new EntityComboBoxController(model, this);
        this.model = model;
        entityComboBoxModel = new EntityComboBoxModel(model);
        this.setModel(entityComboBoxModel);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!textField.getText().isEmpty() && entityComboBoxModel.getSelectedEntity() == null) {
                    textField.requestFocusInWindow();
                }
            }
        });
        this.setEditable(true);

        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        this.modelObserver = new ModelObserver();
        model.addObserver(this.modelObserver);

        this.textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent ke) {
                // Result select key:
                // 37 - Left Arrow
                // 38 - Up Arrow
                // 39 - Right Arrow
                // 40 - Down Arrow
                // 10 - Enter
                if ((ke.getKeyCode() >= 37 && ke.getKeyCode() <= 40) || ke.getKeyCode() == 10) {
                    return;
                }

                entityComboBoxModel.setCurrentValue(textField.getText());

                // Perform search action
                // Model update triggers component revalidation
                if (searchActionListener != null) {
                    searchActionListener.actionPerformed(textField.getText());
                }
            }
        });

        this.textField.addActionListener( e -> {
            // Place into the textField the selected value
            textField.setText(entityComboBoxModel.getSelectedName());
        });
    }

    @Override
    public void updateUI() {
        super.updateUI();
        UIManager.put("ComboBox.squareButton", Boolean.FALSE);
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = super.createArrowButton();
                b.setVisible(false);
                return b;
            }
        });
    }

    private void revalidateResult() {
        revalidate();
        if (textField.hasFocus()) {
            hidePopup();
            showPopup();
        }
    }

    public Entity getSelectedEntity() {
        return entityComboBoxModel.getSelectedEntity();
    }

    public Object getSelectedItem() {
        return entityComboBoxModel.getSelectedItem();
    }

    public void setSelectedId(int id){
        entityComboBoxModel.setSelectedEntity(id);
    }

    public void setSelectedEntity(Entity id){
        entityComboBoxModel.setSelectedEntity(id);
    }

    public void setSearchActionListener(ViewActionListener searchActionListener) {
        this.searchActionListener = searchActionListener;
    }
}
