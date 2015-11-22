package org.lostfan.ktv.model.transform;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.FullRenderedService;

public class RenderedServiceTransformer implements EntityTransformer<RenderedService, FullRenderedService> {

    @Override
    public FullRenderedService transformTo(RenderedService entity) {
        FullRenderedService dto = new FullRenderedService();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setPrice(entity.getPrice());
        dto.setSubscriberId(entity.getSubscriberId());
        dto.setServiceId(entity.getServiceId());
        return dto;
    }

    @Override
    public RenderedService transformFrom(FullRenderedService dto) {
        RenderedService entity = new RenderedService();
        entity.setId(dto.getId());
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setSubscriberId(dto.getSubscriberId());
        entity.setServiceId(dto.getServiceId());
        return entity;
    }
}
