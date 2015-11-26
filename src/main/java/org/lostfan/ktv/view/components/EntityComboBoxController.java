package org.lostfan.ktv.view.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import org.lostfan.ktv.model.searcher.EntitySearcherModel;

public class EntityComboBoxController {

    private EntitySearcherModel model;
    private EntityComboBox view;

    public EntityComboBoxController(EntitySearcherModel model, EntityComboBox view) {
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
            SwingUtilities.invokeLater(() -> {
                    if(view.isReloadComboBoxData(ke)) {
                        model.setSearchQuery(view.getText());
                    }
                    view.keyClick(ke);
                });
        }
    }

    private class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                    view.editTextFieldText();
                    if(view.isReloadComboBoxData()) {
                        model.setSearchQuery(view.getText());
                        view.comboFilter(view.getText());
                    }
                });

        }
    }
}
