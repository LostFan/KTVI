package org.lostfan.ktv.view;


import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;

public class RenderedServiceTableView extends EntityTableView{

    private ViewActionListener addConnectionActionListener;
    private ViewActionListener addDisconnectionActionListener;

    //TODO change newButton to addButton
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
        JButton newButton = new JButton(getString("buttons.add"));
        newButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        newButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    popup.show(e.getComponent(), newButton.getHorizontalTextPosition(), newButton.getVerticalTextPosition() + newButton.getHeight());
                }
            }
        });
        addButton(newButton, false);
    }

    public void setConnectionActionListener(ViewActionListener addConnectionActionListener) {
        this.addConnectionActionListener = addConnectionActionListener;
    }

    public void setDisconnectionActionListener(ViewActionListener addDisconnectionActionListener) {
        this.addDisconnectionActionListener = addDisconnectionActionListener;
    }
}
