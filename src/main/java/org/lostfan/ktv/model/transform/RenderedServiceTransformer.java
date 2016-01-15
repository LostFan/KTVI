package org.lostfan.ktv.model.transform;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.RenderedServiceExt;

public class RenderedServiceTransformer implements EntityTransformer<RenderedService, RenderedServiceExt> {

    @Override
    public RenderedServiceExt transformTo(RenderedService entity) {
        RenderedServiceExt dto = new RenderedServiceExt();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setPrice(entity.getPrice());
        dto.setSubscriberAccount(entity.getSubscriberAccount());
        dto.setServiceId(entity.getServiceId());
        return dto;
    }
}
