package org.lostfan.ktv.view;

import java.awt.event.*;
import javax.swing.*;

import org.lostfan.ktv.controller.ComboBoxController;
import org.lostfan.ktv.model.EntityComboBoxModel;
import org.lostfan.ktv.model.ValueComboBoxModel;
import org.lostfan.ktv.utils.Observer;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class ComboBoxView extends JComboBox{

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            ComboBoxView.this.thisRevalidate();
        }
    }

    private JComboBox jComboBox;
    private JTextField textfield;
    private EntityComboBoxModel model;
    private ModelObserver modelObserver;
    private ValueComboBoxModel valueComboBoxModel;

    public ComboBoxView(EntityComboBoxModel model) {

        textfield = (JTextField)     this.getEditor().getEditorComponent();
        new ComboBoxController(model, this);
        this.model = model;
        valueComboBoxModel = new ValueComboBoxModel(model);
        this.setModel(valueComboBoxModel);
        textfield.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                if(!textfield.getText().isEmpty() && valueComboBoxModel.getSelectedId() == null) {
                    textfield.requestFocusInWindow();
                }
            }
        });
        this.setEditable(true);
        this.modelObserver = new ModelObserver();

        model.addObserver(this.modelObserver);

    }

    private void comboFilter(String enteredText) {
        valueComboBoxModel.setNewModel(model, enteredText);
        if (valueComboBoxModel.getSize() > 0) {
            this.setModel(valueComboBoxModel);
//            getJComboBox().showPopup();
//            this.setSelectedItem(enteredText);
        }
    }

    public String getText() {
        return textfield.getText();
    }

    public Integer getSelectedId() {
        return valueComboBoxModel.getSelectedId();
    }

    public JComboBox getJComboBox() {
        return this;
    }

    public Object getSelectedNameById(int id) {
        return valueComboBoxModel.getSelectedNameById(id);
    }

    public void setId(int id){
        valueComboBoxModel.setId(id);
    }


    public void thisRevalidate() {
        if(this.isPopupVisible()) {
            this.hidePopup();
            this.showPopup();
        }
    }

    public void addLocalKeyListener(KeyListener listener) {
        this.textfield.addKeyListener(listener);
    }

    public void addComboBoxActionListener(ActionListener listener) {
        this.addActionListener(listener);
    }

    public void keyClick(KeyEvent ke) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if ((ke.getKeyCode() < 37 || ke.getKeyCode() > 40) && ke.getKeyCode() != 10) { //up, right, left, down and enter keys
                    comboFilter(textfield.getText());
                }

                boolean popupVisible = true;
                if (valueComboBoxModel.getSize() == 0) {
                    popupVisible = false;
                } else if (valueComboBoxModel.getSelectedId() == null && ke.getKeyCode() == 10) {

                }
                if (ke.getKeyCode() == 10) {
                    if (valueComboBoxModel.getSelectedId() == null) {
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
        if(!getJComboBox().isPopupVisible()) {
            comboFilter(textfield.getText());
        }
        textfield.setText(valueComboBoxModel.getSelectedName());
    }

}
