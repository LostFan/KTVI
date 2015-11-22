package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;

import java.util.List;

public class FullRenderedService extends RenderedService {

    List<MaterialConsumption> materialConsumption;

    public List<MaterialConsumption> getMaterialConsumption() {
        return materialConsumption;
    }

    public void setMaterialConsumption(List<MaterialConsumption> materialConsumption) {
        this.materialConsumption = materialConsumption;
    }
}
