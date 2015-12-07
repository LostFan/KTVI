package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;

import java.util.List;

public class TariffWithPrices extends Tariff {

    private List<TariffPrice> archivePrices;
    private TariffPrice currentPrice;
    private TariffPrice newPrice;

    public List<TariffPrice> getArchivePrices() {
        return archivePrices;
    }

    public void setArchivePrices(List<TariffPrice> archivePrices) {
        this.archivePrices = archivePrices;
    }

    public TariffPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(TariffPrice currentPrice) {
        this.currentPrice = currentPrice;
    }

    public TariffPrice getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(TariffPrice newPrice) {
        this.newPrice = newPrice;
    }
}
