package dao;

import java.util.List;

import domain.Material;


public interface MaterialDAO {

    public List<Material> getAllMaterials();

    public Material getMaterial(String name);

    public void save(Material tariff);

    public void update(Material tariff);

    public void delete(Material tariff);
}
