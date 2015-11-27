package org.lostfan.ktv.model.searcher;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Tariff;

public class TariffSearcherModel extends EntitySearcherModel<Tariff> {

    public TariffSearcherModel() {
    }

    @Override
    public Class getEntityClass() {
        return Tariff.class;
    }

    @Override
    protected EntityDAO<Tariff> getDao() {
        return DAOFactory.getDefaultDAOFactory().getTariffDAO();
    }
}
