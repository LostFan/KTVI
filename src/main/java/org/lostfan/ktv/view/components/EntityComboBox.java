package org.lostfan.ktv.view.components;

import java.awt.event.*;
import javax.swing.*;

import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.utils.Observer;

public class EntityComboBox extends JComboBox<String> {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntityComboBox.this.thisRevalidate();
        }
    }

    private JTextField textField;
    private EntitySearcherModel model;
    private EntityComboBoxModel entityComboBoxModel;

    EntityComboBox(EntitySearcherModel model) {
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
                if (!textField.getText().isEmpty() && entityComboBoxModel.getSelectedId() == null) {
                    textField.requestFocusInWindow();
                }
            }
        });
        this.setEditable(true);

        model.addObserver(new ModelObserver());

    }

    public void comboFilter(String enteredText) {
        entityComboBoxModel.setNewModel(model, enteredText);
        if (entityComboBoxModel.getSize() > 0) {
            this.setModel(entityComboBoxModel);
        }
    }

    public String getText() {
        return textField.getText();
    }

    public Integer getSelectedId() {
        return entityComboBoxModel.getSelectedId();
    }

    public JComboBox getJComboBox() {
        return this;
    }

    public Object getSelectedName() {
        return entityComboBoxModel.getSelectedNameById();
    }

    public void setSelectedId(int id){
        entityComboBoxModel.setSelectedId(id);
    }


    public void thisRevalidate() {
        if(this.isPopupVisible()) {
            this.hidePopup();
            this.showPopup();
        }
    }

    public void addLocalKeyListener(KeyListener listener) {
        this.textField.addKeyListener(listener);
    }

    public void addComboBoxActionListener(ActionListener listener) {
        this.addActionListener(listener);
    }

    public void keyClick(KeyEvent ke) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if ((ke.getKeyCode() < 37 || ke.getKeyCode() > 40) && ke.getKeyCode() != 10) { //up, right, left, down and enter keys
                    comboFilter(textField.getText());
                }

                boolean popupVisible = true;
                if (entityComboBoxModel.getSize() == 0) {
                    popupVisible = false;
                } else if (entityComboBoxModel.getSelectedId() == null && ke.getKeyCode() == 10) {

                }
                if (ke.getKeyCode() == 10) {
                    if (entityComboBoxModel.getSelectedId() == null) {
                        popupVisible = true;
                        getJComboBox().setSelectedIndex(0);

                    } else {
                        popupVisible = false;

                    }
                }

                if (popupVisible) {
                    getJComboBox().showPopup();
                } else {
                    getJComboBox().hidePopup();
                }

                thisRevalidate();

            }
        });
    }

    public boolean isReloadComboBoxData(KeyEvent ke) {
        if(ke.getKeyCode() >= 37 && ke.getKeyCode() <= 40) {
            return false;
        }
        return true;
    }

    public boolean isReloadComboBoxData() {
        return !getJComboBox().isPopupVisible();
    }

    public void editTextFieldText() {
        textField.setText(entityComboBoxModel.getSelectedName());
    }

}
