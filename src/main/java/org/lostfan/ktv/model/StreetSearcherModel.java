package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.StreetDAO;
import org.lostfan.ktv.domain.Street;

import java.util.ArrayList;
import java.util.List;

public class StreetSearcherModel extends EntitySearcherModel<Street> {

    private StreetDAO dao;
    private List<Street> streets;

    public StreetSearcherModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getStreetDAO();
    }

    @Override
    public void setSearchQuery(String query) {
        this.streets = dao.getAllContainsInName(query);
        this.notifyObservers(null);
    }

    @Override
    public List<Street> getList() {
        if (this.streets == null) {
            this.streets = this.dao.getAll();
        }
        return this.streets;
    }

    @Override
    public Class getEntityClass() {
        return Street.class;
    }
}
