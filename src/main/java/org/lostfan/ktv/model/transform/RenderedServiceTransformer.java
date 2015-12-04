package org.lostfan.ktv.model.transform;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;

public class RenderedServiceTransformer implements EntityTransformer<RenderedService, ConnectionRenderedService> {

    @Override
    public ConnectionRenderedService transformTo(RenderedService entity) {
        ConnectionRenderedService dto = new ConnectionRenderedService();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setPrice(entity.getPrice());
        dto.setSubscriberAccount(entity.getSubscriberAccount());
        dto.setServiceId(entity.getServiceId());
        return dto;
    }
}
