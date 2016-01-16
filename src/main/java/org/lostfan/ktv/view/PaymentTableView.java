package org.lostfan.ktv.view;

import java.awt.*;
import java.time.LocalDate;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.PaymentEntityModel;
import org.lostfan.ktv.utils.ViewActionListener;

import javax.swing.*;

public class PaymentTableView extends EntityTableView {

    private ViewActionListener loadActionListener;
    private ViewActionListener newDateActionListener;

    public PaymentTableView(PaymentEntityModel model) {
        super(model);
        JButton priceButton = new JButton(getGuiString("buttons.load"));
        priceButton.addActionListener(e -> {
            if (loadActionListener != null) {
                loadActionListener.actionPerformed(null);
            }
        });
        addButton(priceButton, false);

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

    public void loadActionListener(ViewActionListener loadActionListener) {
        this.loadActionListener = loadActionListener;
    }

    public void newDateActionListener(ViewActionListener newDateActionListener) {
        this.newDateActionListener = newDateActionListener;
    }
}
