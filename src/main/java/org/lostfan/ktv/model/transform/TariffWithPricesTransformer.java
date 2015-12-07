package org.lostfan.ktv.model.transform;

import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.dto.TariffWithPrices;

public class TariffWithPricesTransformer implements EntityTransformer<Tariff, TariffWithPrices> {

    @Override
    public TariffWithPrices transformTo(Tariff entity) {
        TariffWithPrices tariffWithPrices = new TariffWithPrices();
        tariffWithPrices.setId(entity.getId());
        tariffWithPrices.setName(entity.getName());
        tariffWithPrices.setDigital(entity.isDigital());
        tariffWithPrices.setChannels(entity.getChannels());
        return tariffWithPrices;
    }
}
