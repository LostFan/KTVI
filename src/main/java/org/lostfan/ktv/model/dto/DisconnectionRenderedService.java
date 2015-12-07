package org.lostfan.ktv.model.dto;


import java.util.List;

import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.FixedServices;

public class DisconnectionRenderedService extends RenderedService {


    @Override
    public Integer getServiceId() {
        return FixedServices.DISCONNECTION.getId();
    }

    public static DisconnectionRenderedService build(RenderedService renderedService) {
        DisconnectionRenderedService dto = new DisconnectionRenderedService();
        dto.setId(renderedService.getId());
        dto.setDate(renderedService.getDate());
        dto.setPrice(renderedService.getPrice());
        dto.setSubscriberAccount(renderedService.getSubscriberAccount());
        dto.setServiceId(renderedService.getServiceId());
        return dto;
    }
}
