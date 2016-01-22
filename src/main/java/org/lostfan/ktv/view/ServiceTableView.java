package org.lostfan.ktv.view;

import javax.swing.*;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;

public class ServiceTableView extends EntityTableView {

    private ViewActionListener priceActionListener;

    public ServiceTableView(EntityModel<? extends Entity> model) {
        super(model);

        JButton priceButton = new JButton(getGuiString("buttons.servicePrice"));
        priceButton.addActionListener(e -> {
            if (priceActionListener != null) {
                priceActionListener.actionPerformed(getSelectedEntityId());
            }
        });
        addButton(priceButton, true);
    }

    public void setPriceActionListener(ViewActionListener priceActionListener) {
        this.priceActionListener = priceActionListener;
    }
}
