package org.lostfan.ktv.model.searcher;

import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

public class ServiceSearcherModel extends EntitySearcherModel<Service> {

    private ServiceDAO dao;
    private List<Service> services;

    public ServiceSearcherModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    }

    @Override
    public void setSearchQuery(String query) {
        this.services = dao.getServicesByBeginningPartOfName(query);
        this.notifyObservers(null);
    }

    @Override
    public List<Service> getList() {
        if (this.services == null) {
            this.services = this.dao.getAll();
        }
        return this.services;
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }
}
