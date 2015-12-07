package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.entity.TariffEntityModel;
import org.lostfan.ktv.view.TariffPriceView;
import org.lostfan.ktv.view.TariffTableView;

public class TariffEntityController extends EntityController {

    private TariffEntityModel model;

    public TariffEntityController(TariffEntityModel model, TariffTableView view) {
        super(model, view);
        this.model = model;
        view.setPriceActionListener(this::priceActionPerformed);
    }

    private void priceActionPerformed(Object args) {
        Integer id = (Integer) args;
        TariffPriceView tariffPriceView = new TariffPriceView(model.getTariffWithPrices(id));
        tariffPriceView.setSaveActionListener(this::saveTariffPriceActionPerformed);
    }

    private void saveTariffPriceActionPerformed(Object args) {
        TariffPrice tariffPrice = (TariffPrice) args;
        this.model.save(tariffPrice);
    }
}
