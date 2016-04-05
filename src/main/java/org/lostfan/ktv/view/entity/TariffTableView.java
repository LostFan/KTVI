package org.lostfan.ktv.view.entity;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.table.EntityTableView;

import javax.swing.*;

public class TariffTableView extends EntityTableView {

    private ViewActionListener priceActionListener;

    public TariffTableView(EntityModel<? extends Entity> model) {
        super(model);

        JButton priceButton = new JButton(getGuiString("buttons.tariffPrice"));
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
