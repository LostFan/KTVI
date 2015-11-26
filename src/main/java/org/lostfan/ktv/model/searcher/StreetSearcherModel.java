package org.lostfan.ktv.model.searcher;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Street;

public class StreetSearcherModel extends EntitySearcherModel<Street> {

    public StreetSearcherModel() {
    }

    @Override
    public Class getEntityClass() {
        return Street.class;
    }

    @Override
    protected EntityDAO<Street> getDao() {
        return DAOFactory.getDefaultDAOFactory().getStreetDAO();
    }
}
