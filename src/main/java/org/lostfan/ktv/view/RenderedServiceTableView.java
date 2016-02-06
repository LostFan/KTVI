package org.lostfan.ktv.view;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import javax.swing.*;

import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.Error;

public class RenderedServiceTableView extends EntityTableView {

    private ViewActionListener addConnectionActionListener;
    private ViewActionListener addReconnectionActionListener;
    private ViewActionListener addDisconnectionActionListener;
    private ViewActionListener addChangeOfTariffActionListener;
    private ViewActionListener addAdditionalServiceActionListener;
    private ViewActionListener newDateActionListener;

    public RenderedServiceTableView(RenderedServiceEntityModel model) {
        super(model);
        final JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem(new AbstractAction(getEntityString(FixedServices.CONNECTION.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addConnectionActionListener != null) {
                    addConnectionActionListener.actionPerformed(null);
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction(getEntityString(FixedServices.RECONNECTION.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addReconnectionActionListener != null) {
                    addReconnectionActionListener.actionPerformed(null);
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction(getEntityString(FixedServices.DISCONNECTION.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addDisconnectionActionListener != null) {
                    addDisconnectionActionListener.actionPerformed(null);
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction(getEntityString(FixedServices.CHANGE_OF_TARIFF.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addChangeOfTariffActionListener != null) {
                    addChangeOfTariffActionListener.actionPerformed(null);
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction(getEntityString(FixedServices.ADDITIONAL_SERVICE.getCode())) {
            public void actionPerformed(ActionEvent e) {
                if (addAdditionalServiceActionListener != null) {
                    addAdditionalServiceActionListener.actionPerformed(null);
                }
            }
        }));
        JButton addButton = getButton(getGuiString("buttons.add"));
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
        JButton beginButton =  new JButton("<<<");
        JButton minusYear =  new JButton("<<");
        JButton minusMonth =  new JButton("<");
        JButton plusMonth =  new JButton(">");
        JButton plusYear =  new JButton(">>");
        JButton endButton =  new JButton(">>>");
        JTextArea numberOfMonth = new JTextArea();
        numberOfMonth.setText(String.valueOf(model.getDate().getMonth().getValue()));
        numberOfMonth.setOpaque(false);
        numberOfMonth.setBackground(new Color(0,0,0,0));
        JTextArea numberOfYear = new JTextArea();
        numberOfYear.setText(String.valueOf(model.getDate().getYear()));
        numberOfYear.setOpaque(false);
        numberOfYear.setBackground(new Color(0,0,0,0));
        numberOfYear.setForeground(null);


        minusMonth.addActionListener(e -> {
            LocalDate date = model.getDate().minusMonths(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });
        minusYear.addActionListener(e -> {
            LocalDate date = model.getDate().minusYears(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });
        plusMonth.addActionListener(e -> {
            LocalDate date = model.getDate().plusMonths(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });
        plusYear.addActionListener(e -> {
            LocalDate date = model.getDate().plusYears(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });



        JPanel newPanel = new JPanel();
        getContentPanel().add(newPanel,  BorderLayout.SOUTH);
        newPanel.add(beginButton);
        newPanel.add(minusYear);
        newPanel.add(minusMonth);
        newPanel.add(numberOfMonth);
        newPanel.add(numberOfYear);
        newPanel.add(plusMonth);
        newPanel.add(plusYear);
        newPanel.add(endButton);
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

    public void setAdditionalServiceActionListener(ViewActionListener addAdditionalServiceActionListener) {
        this.addAdditionalServiceActionListener = addAdditionalServiceActionListener;
    }

    public void setReconnectionActionListener(ViewActionListener addReconnectionActionListener) {
        this.addReconnectionActionListener = addReconnectionActionListener;
    }

    public void newDateActionListener(ViewActionListener newDateActionListener) {
        this.newDateActionListener = newDateActionListener;
    }

    public void errorWindow(Iterable<Error> errors) {
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.ERROR_MESSAGE;
        Object[] selValues = { getGuiString("buttons.ok") };
        String message = getGuiString("window.deleteFailed");
        StringBuffer stringBuffer = new StringBuffer();
        for (Error error : errors) {
            if (error.getField() == null) {
                stringBuffer.append(error.getMessage());
                stringBuffer.append("\n");
            }
        }
        JOptionPane.showOptionDialog(null,
                stringBuffer, message,
                optionType, messageType, null, selValues,
                selValues[0]);
    }
}
