package org.lostfan.ktv.dao;

import java.util.List;

import org.lostfan.ktv.domain.Material;


public interface MaterialDAO {

    List<Material> getAllMaterials();

    Material getMaterial(int id);

    void save(Material material);

    void update(Material material);

    void delete(int id);
}
