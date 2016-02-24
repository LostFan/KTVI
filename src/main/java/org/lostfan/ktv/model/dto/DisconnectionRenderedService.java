package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.model.FixedServices;

public class DisconnectionRenderedService extends RenderedService {

    private Integer disconnectionReasonId;

    @Override
    public Integer getServiceId() {
        return FixedServices.DISCONNECTION.getId();
    }

    public Integer getDisconnectionReasonId() {
        return this.disconnectionReasonId;
    }

    public void setDisconnectionReasonId(Integer disconnectionReasonId) {
        this.disconnectionReasonId = disconnectionReasonId;
    }

    public static DisconnectionRenderedService build(RenderedService renderedService, SubscriberSession subscriberSession) {
        DisconnectionRenderedService dto = new DisconnectionRenderedService();
        dto.setId(renderedService.getId());
        dto.setDate(renderedService.getDate());
        dto.setPrice(renderedService.getPrice());
        dto.setSubscriberAccount(renderedService.getSubscriberAccount());
        dto.setServiceId(renderedService.getServiceId());
        dto.setDisconnectionReasonId(subscriberSession.getDisconnectionReasonId());
        return dto;
    }
}
