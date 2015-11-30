package org.lostfan.ktv.model.transform;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.FullRenderedService;

public class RenderedServiceTransformer implements EntityTransformer<RenderedService, FullRenderedService> {

    @Override
    public FullRenderedService transformTo(RenderedService entity) {
        FullRenderedService dto = new FullRenderedService();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setPrice(entity.getPrice());
        dto.setSubscriberAccount(entity.getSubscriberAccount());
        dto.setServiceId(entity.getServiceId());
        return dto;
    }

    @Override
    public FullRenderedService transformTo(RenderedService entity, Map<String, List<Entity>> map) {
        FullRenderedService dto = transformTo(entity);
        dto.setMaterialConsumption(map.get("materialConsumption").stream().map(e -> (MaterialConsumption) e).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public RenderedService transformFrom(FullRenderedService dto) {
        RenderedService entity = new RenderedService();
        entity.setId(dto.getId());
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setSubscriberAccount(dto.getSubscriberAccount());
        entity.setServiceId(dto.getServiceId());
        return entity;
    }
}
