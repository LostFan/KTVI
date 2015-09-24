package org.lostfan.ktv.dao;

import java.util.List;

import org.lostfan.ktv.domain.Material;
import org.lostfan.ktv.domain.MaterialConsumption;


public interface MaterialDAO {

    List<Material> getAllMaterials();

    Material getMaterial(int id);

    void save(Material material);

    void update(Material material);

    void delete(int id);

    List<MaterialConsumption> getAllMaterialConsumptions();

    List<MaterialConsumption> getMaterialConsumptionsByRenderedServiceId(int renderedServiceId);

    MaterialConsumption getMaterialConsumption(int id);

    void saveMaterialConsumption(MaterialConsumption materialConsumption);

    void updateMaterialConsumption(MaterialConsumption materialConsumption);

    void deleteMaterialConsumption(int materialConsumptionId);
}
