package org.lostfan.ktv.dao;

import org.lostfan.ktv.domain.MaterialConsumption;

import java.util.List;


public interface MaterialConsumptionDAO extends EntityDAO<MaterialConsumption> {

    List<MaterialConsumption> getMaterialConsumptionsByRenderedServiceId(int renderedServiceId);
}
