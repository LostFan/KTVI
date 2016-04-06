package org.lostfan.ktv.model.dto;


import java.util.List;

import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FixedServices;

public class MaterialsRenderedService extends RenderedService implements MaterialsDTO {

    private List<MaterialConsumption> materialConsumption;


    @Override
    public Integer getServiceId() {
        return FixedServices.MATERIALS.getId();
    }

    public static MaterialsRenderedService build(RenderedService renderedService,List<MaterialConsumption> materialConsumptions) {
        MaterialsRenderedService dto = new MaterialsRenderedService();
        dto.setId(renderedService.getId());
        dto.setDate(renderedService.getDate());
        dto.setPrice(renderedService.getPrice());
        dto.setSubscriberAccount(renderedService.getSubscriberAccount());
        dto.setMaterialConsumption(materialConsumptions);
        return dto;
    }

    public List<MaterialConsumption> getMaterialConsumption() {
        return materialConsumption;
    }

    public void setMaterialConsumption(List<MaterialConsumption> materialConsumption) {
        this.materialConsumption = materialConsumption;
    }
}
