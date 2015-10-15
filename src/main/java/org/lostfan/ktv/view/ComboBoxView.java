package org.lostfan.ktv.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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
        this.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                textfield.setText(box.getSelectedItem().toString());
            }
        });
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
//            this.setSelectedItem(enteredText);
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

    public void keyClick(KeyEvent ke) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if((ke.getKeyCode() < 37 || ke.getKeyCode() > 40)) { //up, right, left, down and enter keys
                    comboFilter(textfield.getText());
                }
                if(ke.getKeyCode() == 10) {
                    getJComboBox().hidePopup();
                }
                else {
                    getJComboBox().showPopup();
                }
            }
        });
    }

    public boolean isReloadComboBoxData(KeyEvent ke) {
        if(ke.getKeyCode() >= 37 && ke.getKeyCode() <= 40) {
            return false;
        }
        if(ke.getKeyCode() == 10) {
            textfield.setText(valueComboBoxModel.getSelectedName());
        }
        return true;
    }
}
