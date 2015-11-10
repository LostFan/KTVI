package org.lostfan.ktv.model.searcher;

import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.domain.RenderedService;

public class RenderedServiceSearcherModel extends EntitySearcherModel<RenderedService> {

    private RenderedServiceDAO dao;
    private List<RenderedService> renderedServices;


    public RenderedServiceSearcherModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    }

    @Override
    public void setSearchQuery(String query) {
        this.renderedServices = dao.getAllContainsInName(query);
        this.notifyObservers(null);
    }

    @Override
    public List<RenderedService> getList() {
        if (this.renderedServices == null) {
            this.renderedServices = this.dao.getAll();
        }
        return this.renderedServices;
    }

    @Override
    public Class getEntityClass() {
        return RenderedService.class;
    }
}
