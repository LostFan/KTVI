package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.view.TariffPriceView;
import org.lostfan.ktv.view.TariffTableView;

public class TariffEntityController extends EntityController {

    public TariffEntityController(EntityModel model, TariffTableView view) {
        super(model, view);
        view.setPriceActionListener(this::priceActionPerformed);
    }

    private void priceActionPerformed(Object args) {
        new TariffPriceView(null);
    }
}
