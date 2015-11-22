package org.lostfan.ktv.model.transform;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.FullRenderedService;

public class RenderedServiceTransformer implements EntityTransformer<RenderedService, FullRenderedService> {

    @Override
    public FullRenderedService transformTo(RenderedService entity) {
        FullRenderedService dto = new FullRenderedService();
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public RenderedService transformFrom(FullRenderedService dto) {
        RenderedService entity = new RenderedService();
        entity.setId(dto.getId());
        return entity;
    }
}
