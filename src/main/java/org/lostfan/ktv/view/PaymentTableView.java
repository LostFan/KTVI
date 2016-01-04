package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;

import javax.swing.*;

public class PaymentTableView extends EntityTableView {

    private ViewActionListener loadActionListener;

    public PaymentTableView(EntityModel<? extends Entity> model) {
        super(model);

        JButton priceButton = new JButton(getGuiString("buttons.load"));
        priceButton.addActionListener(e -> {
            if (loadActionListener != null) {
                loadActionListener.actionPerformed(null);
            }
        });
        addButton(priceButton, false);
    }

    public void loadActionListener(ViewActionListener loadActionListener) {
        this.loadActionListener = loadActionListener;
    }
}
