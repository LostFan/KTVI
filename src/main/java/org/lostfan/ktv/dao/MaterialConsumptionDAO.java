package org.lostfan.ktv.dao;

import org.lostfan.ktv.domain.MaterialConsumption;

import java.util.List;


public interface MaterialConsumptionDAO extends EntityDAO<MaterialConsumption> {

    List<MaterialConsumption> getAll();

    List<MaterialConsumption> getMaterialConsumptionsByRenderedServiceId(int renderedServiceId);

    MaterialConsumption get(int id);

    void save(MaterialConsumption materialConsumption);

    void update(MaterialConsumption materialConsumption);

    void delete(int materialConsumptionId);

    List<MaterialConsumption> getAllContainsInName(String str);
}
