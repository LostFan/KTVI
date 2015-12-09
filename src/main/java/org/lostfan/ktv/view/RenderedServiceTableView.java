package org.lostfan.ktv.view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;

public class RenderedServiceTableView extends EntityTableView{

    private ViewActionListener addConnectionActionListener;
    private ViewActionListener addDisconnectionActionListener;
    private ViewActionListener addChangeOfTariffActionListener;

    public RenderedServiceTableView(RenderedServiceEntityModel model) {
        super(model);
        final JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem(new AbstractAction(ResourceBundles.getEntityBundle().getString(FixedServices.CONNECTION.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addConnectionActionListener != null) {
                    addConnectionActionListener.actionPerformed(null);
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction(ResourceBundles.getEntityBundle().getString(FixedServices.DISCONNECTION.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addDisconnectionActionListener != null) {
                    addDisconnectionActionListener.actionPerformed(null);
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction(ResourceBundles.getEntityBundle().getString(FixedServices.CHANGE_OF_TARIFF.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addChangeOfTariffActionListener != null) {
                    addChangeOfTariffActionListener.actionPerformed(null);
                }
            }
        }));
        JButton addButton = getButton(getString("buttons.add"));
        for (ActionListener actionListener : addButton.getActionListeners()) {
            addButton.removeActionListener(actionListener);
        }
        addButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        addButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    popup.show(e.getComponent(), addButton.getHorizontalTextPosition(), addButton.getVerticalTextPosition() + addButton.getHeight());
                }
            }
        });
    }

    public void setConnectionActionListener(ViewActionListener addConnectionActionListener) {
        this.addConnectionActionListener = addConnectionActionListener;
    }

    public void setDisconnectionActionListener(ViewActionListener addDisconnectionActionListener) {
        this.addDisconnectionActionListener = addDisconnectionActionListener;
    }

    public void setChangeOfTariffActionListener(ViewActionListener addChangeOfTariffActionListener) {
        this.addChangeOfTariffActionListener = addChangeOfTariffActionListener;
    }
}
