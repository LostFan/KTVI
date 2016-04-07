package org.lostfan.ktv.view.table;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.components.TextField;

public class SubscriberTableView extends EntityTableView {

    class MultiKeyPressListener implements KeyListener {

        // Set of currently pressed keys
        private final Set<Integer> pressed = new HashSet<>();

        @Override
        public synchronized void keyPressed(KeyEvent e) {
            pressed.add(e.getKeyCode());
            if (pressed.size() > 1) {
                // "ALT + F3" keys
                if (pressed.contains(18) && pressed.contains(114)) {
                    int selectedId = getSelectedEntityId();
                    if (selectedId != -1 && SubscriberTableView.this.balanceActionListener != null) {
                        SubscriberTableView.this.balanceActionListener.actionPerformed(selectedId);
                    }
                }
                pressed.clear();
            }
        }

        @Override
        public synchronized void keyReleased(KeyEvent e) {
            pressed.remove(e.getKeyCode());
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    private ViewActionListener findActionListener;

    private ViewActionListener balanceActionListener;

    public SubscriberTableView(SubscriberEntityModel model) {
        super(model);
        JPanel searchPanel = new JPanel();
        JLabel label = new JLabel(getGuiString("label.search"));
        searchPanel.add(label);
        TextField textField = new TextField(15);
        textField.addTextChangeListener(e ->  {
            if (findActionListener != null) {
                findActionListener.actionPerformed(textField.getText());
            }
        });
        searchPanel.add(textField);

        JButton findButton = new JButton(getGuiString("buttons.find"));

        findButton.addActionListener(e -> {
            if (findActionListener != null) {
                findActionListener.actionPerformed(textField.getText());
            }
        }
        );
        searchPanel.add(findButton);

        JButton balanceButton = new JButton(getGuiString("buttons.subscriberInformation"));

        balanceButton.addActionListener(e -> {
                    if (balanceActionListener != null) {
                        balanceActionListener.actionPerformed(getSelectedEntityId());
                    }
                }
        );
        addButton(balanceButton, true);

        this.tablePanel.add(searchPanel, BorderLayout.NORTH);

        this.table.addKeyListener(new MultiKeyPressListener());
    }

    public void setFindActionListener(ViewActionListener findActionListener) {
        this.findActionListener = findActionListener;
    }

    public void setBalanceActionListener(ViewActionListener balanceActionListener) {
        this.balanceActionListener = balanceActionListener;
    }
}
