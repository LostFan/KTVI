package org.lostfan.ktv.model.dto;


import java.util.List;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;

public interface MaterialsDTO extends Entity {

    List<MaterialConsumption> getMaterialConsumption();

    void setMaterialConsumption(List<MaterialConsumption> materialConsumption);

}
