package org.lostfan.ktv.dao;

import java.util.List;

import org.lostfan.ktv.domain.Material;
import org.lostfan.ktv.domain.MaterialConsumption;


public interface MaterialDAO extends EntityDAO<Material> {

    List<Material> getAll();

    Material get(int id);

    Material save(Material material);

    Material update(Material material);

    void delete(int id);

    List<Material> getAllContainsInName(String str);
}
