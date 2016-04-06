package org.lostfan.ktv.model.dto;


import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.FixedServices;

public class ConnectionRenderedService extends RenderedService implements TariffField {

    private Integer tariffId;

    public static ConnectionRenderedService build(RenderedService renderedService, SubscriberTariff subscriberTariff) {
        ConnectionRenderedService dto = new ConnectionRenderedService();
        dto.setId(renderedService.getId());
        dto.setDate(renderedService.getDate());
        dto.setPrice(renderedService.getPrice());
        dto.setSubscriberAccount(renderedService.getSubscriberAccount());
        dto.setServiceId(renderedService.getServiceId());
        dto.setTariffId(subscriberTariff.getTariffId());
        return dto;
    }

    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    @Override
    public Integer getServiceId() {
        return FixedServices.CONNECTION.getId();
    }
}
