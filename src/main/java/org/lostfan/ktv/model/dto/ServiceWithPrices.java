package org.lostfan.ktv.model.dto;

import java.util.List;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;

public class ServiceWithPrices extends Service {

    private List<ServicePrice> archivePrices;
    private ServicePrice currentPrice;
    private ServicePrice newPrice;

    public List<ServicePrice> getArchivePrices() {
        return archivePrices;
    }

    public void setArchivePrices(List<ServicePrice> archivePrices) {
        this.archivePrices = archivePrices;
    }

    public ServicePrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ServicePrice currentPrice) {
        this.currentPrice = currentPrice;
    }

    public ServicePrice getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(ServicePrice newPrice) {
        this.newPrice = newPrice;
    }
}
