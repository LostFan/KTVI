package org.lostfan.ktv.model.searcher;

import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

public class ServiceSearcherModel extends EntitySearcherModel<Service> {

    public ServiceSearcherModel() {
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }

    @Override
    protected EntityDAO<Service> getDao() {
        return DAOFactory.getDefaultDAOFactory().getServiceDAO();
    }
}
