package org.lostfan.ktv.model.dto;


import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.SubscriberEntityModel;

public class ConnectionRenderedService  extends RenderedService {

    List<MaterialConsumption> materialConsumption;

    public List<MaterialConsumption> getMaterialConsumption() {
        return materialConsumption;
    }

    public void setMaterialConsumption(List<MaterialConsumption> materialConsumption) {
        this.materialConsumption = materialConsumption;
    }

    private Integer tariffId;

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

    public static ConnectionRenderedService build(RenderedService renderedService, SubscriberTariff subscriberTariff, List<MaterialConsumption> materialConsumptions) {
        ConnectionRenderedService dto = new ConnectionRenderedService();
        dto.setId(renderedService.getId());
        dto.setDate(renderedService.getDate());
        dto.setPrice(renderedService.getPrice());
        dto.setSubscriberAccount(renderedService.getSubscriberAccount());
        dto.setServiceId(renderedService.getServiceId());
        dto.setTariffId(subscriberTariff.getTariffId());
        dto.setMaterialConsumption(materialConsumptions);
        return dto;
    }
}
