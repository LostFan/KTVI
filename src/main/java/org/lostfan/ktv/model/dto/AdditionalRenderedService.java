package org.lostfan.ktv.model.dto;


import java.util.List;

import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;

public class AdditionalRenderedService extends RenderedService implements MaterialsDTO {

    private List<MaterialConsumption> materialConsumption;

    public List<MaterialConsumption> getMaterialConsumption() {
        return materialConsumption;
    }

    public void setMaterialConsumption(List<MaterialConsumption> materialConsumption) {
        this.materialConsumption = materialConsumption;
    }

    public static AdditionalRenderedService build(RenderedService renderedService, SubscriberTariff subscriberTariff, List<MaterialConsumption> materialConsumptions) {
        AdditionalRenderedService dto = new AdditionalRenderedService();
        dto.setId(renderedService.getId());
        dto.setDate(renderedService.getDate());
        dto.setPrice(renderedService.getPrice());
        dto.setSubscriberAccount(renderedService.getSubscriberAccount());
        dto.setServiceId(renderedService.getServiceId());
        dto.setMaterialConsumption(materialConsumptions);
        return dto;
    }
}
