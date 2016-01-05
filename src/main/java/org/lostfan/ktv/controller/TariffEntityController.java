package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.entity.TariffEntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.TariffPriceView;
import org.lostfan.ktv.view.TariffTableView;

public class TariffEntityController extends EntityController {

    private TariffEntityModel model;
    private TariffPriceView tariffPriceView;

    public TariffEntityController(TariffEntityModel model, TariffTableView view) {
        super(model, view);
        this.model = model;
        view.setPriceActionListener(this::priceActionPerformed);
    }

    private void priceActionPerformed(Object args) {
        Integer id = (Integer) args;
        this.tariffPriceView = new TariffPriceView(model.getTariffWithPrices(id));
        this.tariffPriceView.setSaveActionListener(this::saveTariffPriceActionPerformed);
    }

    private void saveTariffPriceActionPerformed(Object args) {
        TariffPrice tariffPrice = (TariffPrice) args;
        ValidationResult result = this.model.save(tariffPrice);
        if (result.hasErrors()) {
            this.tariffPriceView.showErrors(result);
        } else {
            this.tariffPriceView.hide();
        }
    }
}
