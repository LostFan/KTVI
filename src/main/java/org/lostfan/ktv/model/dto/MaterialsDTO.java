package org.lostfan.ktv.model.dto;


import java.util.List;

import org.lostfan.ktv.domain.MaterialConsumption;

public interface MaterialsDTO {

    List<MaterialConsumption> getMaterialConsumption();

    void setMaterialConsumption(List<MaterialConsumption> materialConsumption);

}
