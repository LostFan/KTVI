package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;

import java.util.List;

public interface MaterialConsumptionTable {


    public List<MaterialConsumption> getMaterialConsumption();

    public void setMaterialConsumption(List<MaterialConsumption> materialConsumption);

    Integer getId();

}
