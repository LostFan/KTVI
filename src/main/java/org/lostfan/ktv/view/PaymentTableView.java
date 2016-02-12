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

    private JLabel monthNumber;
    private JLabel yearNumber;

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
        this.monthNumber = new JLabel(String.valueOf(model.getDate().getMonth().getValue()));
        this.yearNumber = new JLabel(String.valueOf(model.getDate().getYear()));

        minusMonth.addActionListener(e -> updateDate(model.getDate().minusMonths(1)));
        minusYear.addActionListener(e -> updateDate(model.getDate().minusYears(1)));
        plusMonth.addActionListener(e -> updateDate(model.getDate().plusMonths(1)));
        plusYear.addActionListener(e -> updateDate(model.getDate().plusYears(1)));

        JPanel newPanel = new JPanel();
        getContentPanel().add(newPanel,  BorderLayout.SOUTH);
        newPanel.add(beginButton);
        newPanel.add(minusYear);
        newPanel.add(minusMonth);
        newPanel.add(this.monthNumber);
        newPanel.add(new JLabel("/"));
        newPanel.add(this.yearNumber);
        newPanel.add(plusMonth);
        newPanel.add(plusYear);
        newPanel.add(endButton);
    }

    private void updateDate(LocalDate newDate) {
        this.monthNumber.setText(String.valueOf(newDate.getMonth().getValue()));
        this.yearNumber.setText(String.valueOf(newDate.getYear()));
        if (newDateActionListener != null) {
            newDateActionListener.actionPerformed(newDate);
        }
        revalidate();
    }

    public void loadActionListener(ViewActionListener loadActionListener) {
        this.loadActionListener = loadActionListener;
    }

    public void newDateActionListener(ViewActionListener newDateActionListener) {
        this.newDateActionListener = newDateActionListener;
    }
}
