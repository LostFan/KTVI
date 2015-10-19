package org.lostfan.ktv.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import org.lostfan.ktv.model.EntityComboBoxModel;
import org.lostfan.ktv.view.ComboBoxView;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class ComboBoxController {

    private EntityComboBoxModel model;
    private ComboBoxView view;

    public ComboBoxController(EntityComboBoxModel model, ComboBoxView view) {
        this.model = model;
        this.view = view;
        this.view.addLocalKeyListener(new LocalKeyListener());
        this.view.addComboBoxActionListener(new ComboBoxActionListener());
    }

    private class LocalKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent ke) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    view.keyClick(ke);
                    if(view.isReloadComboBoxData(ke)) {
                        model.setListByBeginningPartOfName(view.getText());
                    }
                }
            });
        }
    }

    private class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    view.editTextFieldText();
                    if(view.isReloadComboBoxData()) {
                        model.setListByBeginningPartOfName(view.getText());
                    }
                }
            });

        }
    }
}
