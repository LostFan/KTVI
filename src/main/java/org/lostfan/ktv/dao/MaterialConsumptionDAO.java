package org.lostfan.ktv.dao;

import java.util.List;

import org.lostfan.ktv.domain.MaterialConsumption;

/**
 * Created by Ihar_Niakhlebau on 14-Sep-15.
 */
public interface MaterialConsumptionDAO {

    public List<MaterialConsumption> getAllMaterialConsumptions();

    public MaterialConsumption getMaterialConsumption(String name);

    public void save(MaterialConsumption materialConsumption);

    public void update(MaterialConsumption materialConsumption);

    public void delete(MaterialConsumption materialConsumption);
}
