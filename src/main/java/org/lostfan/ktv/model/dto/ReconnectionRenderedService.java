package org.lostfan.ktv.model.dto;


import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.FixedServices;

import java.util.List;

public class ReconnectionRenderedService extends RenderedService implements TariffField {

    private Integer tariffId;


    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    @Override
    public Integer getServiceId() {
        return FixedServices.RECONNECTION.getId();
    }

    public static ReconnectionRenderedService build(RenderedService renderedService, SubscriberTariff subscriberTariff) {
        ReconnectionRenderedService dto = new ReconnectionRenderedService();
        dto.setId(renderedService.getId());
        dto.setDate(renderedService.getDate());
        dto.setPrice(renderedService.getPrice());
        dto.setSubscriberAccount(renderedService.getSubscriberAccount());
        dto.setServiceId(renderedService.getServiceId());
        dto.setTariffId(subscriberTariff.getTariffId());
        return dto;
    }
}
