package org.lostfan.ktv.model;

import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.MaterialDAO;
import org.lostfan.ktv.domain.Material;

public class MaterialSearcherModel extends EntitySearcherModel<Material> {

    private MaterialDAO dao;
    private List<Material> materials;


    public MaterialSearcherModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getMaterialDAO();
    }

    @Override
    public void setSearchQuery(String query) {
        this.materials = dao.getAllContainsInName(query);
        this.notifyObservers(null);
    }

    @Override
    public List<Material> getList() {
        if (this.materials == null) {
            this.materials = this.dao.getAll();
        }
        return this.materials;
    }

    @Override
    public Class getEntityClass() {
        return Material.class;
    }
}
