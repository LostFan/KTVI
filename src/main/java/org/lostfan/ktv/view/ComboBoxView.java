package org.lostfan.ktv.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import org.lostfan.ktv.controller.ComboBoxController;
import org.lostfan.ktv.model.EntityComboBoxModel;
import org.lostfan.ktv.model.ValueComboBoxModel;
import org.lostfan.ktv.utils.Observer;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class ComboBoxView extends JComboBox{

//    private class ModelObserver implements Observer {
//        @Override
//        public void update(Object args) {
//            ComboBoxView.this.revalidate();
//        }
//    }

    private JComboBox jComboBox;
    private JTextField textfield;
    private EntityComboBoxModel model;
//    private ModelObserver modelObserver;
    private ValueComboBoxModel valueComboBoxModel;

    public ComboBoxView(EntityComboBoxModel model) {

        textfield = (JTextField)     this.getEditor().getEditorComponent();
//        this.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//
//                JComboBox box = (JComboBox) e.getSource();
//                System.out.println("!!!! " + box.getSelectedItem().toString());
//                textfield.setText("Подключение");
//            }
//        });
        new ComboBoxController(model, this);
        this.model = model;
        valueComboBoxModel = new ValueComboBoxModel(model);
        this.setModel(valueComboBoxModel);
        this.setEditable(true);
//        this.modelObserver = new ModelObserver();
//
//        model.addObserver(this.modelObserver);

    }

    private void comboFilter(String enteredText) {
        valueComboBoxModel = new ValueComboBoxModel(model, enteredText);
        if (valueComboBoxModel.getSize() > 0) {
            this.setModel(valueComboBoxModel);
            getJComboBox().showPopup();
//            this.setSelectedItem(enteredText);
        } else {
            getJComboBox().hidePopup();
        }
    }

    public String getText() {
        return textfield.getText();
    }

    public JComboBox getJComboBox() {
        return this;
    }

    public Object getSelectedNameById(int id) {
        return valueComboBoxModel.getSelectedNameById(id);
    }


//    private void revalidate() {
//        this.jComboBox.invalidate();
//        this.jComboBox.repaint();
//    }

    public void addLocalKeyListener(KeyListener listener) {
        this.textfield.addKeyListener(listener);
    }

    public void addComboBoxActionListener(ActionListener listener) {
        this.addActionListener(listener);
    }

    public void keyClick(KeyEvent ke) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if((ke.getKeyCode() < 37 || ke.getKeyCode() > 40)) { //up, right, left, down and enter keys
                    comboFilter(textfield.getText());
                }
                if(ke.getKeyCode() == 10) {
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

    public void editTextFieldText() {
        textfield.setText(valueComboBoxModel.getSelectedName());
    }

}
