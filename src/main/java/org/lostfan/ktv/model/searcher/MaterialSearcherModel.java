package org.lostfan.ktv.model.searcher;

import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.MaterialDAO;
import org.lostfan.ktv.domain.Material;

public class MaterialSearcherModel extends EntitySearcherModel<Material> {


    public MaterialSearcherModel() {
    }

    @Override
    public Class getEntityClass() {
        return Material.class;
    }

    @Override
    protected EntityDAO<Material> getDao() {
        return DAOFactory.getDefaultDAOFactory().getMaterialDAO();
    }
}
