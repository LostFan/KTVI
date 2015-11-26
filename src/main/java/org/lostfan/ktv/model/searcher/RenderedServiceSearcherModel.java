package org.lostfan.ktv.model.searcher;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.RenderedService;

public class RenderedServiceSearcherModel extends EntitySearcherModel<RenderedService> {

    public RenderedServiceSearcherModel() {
    }

    @Override
    public Class getEntityClass() {
        return RenderedService.class;
    }

    @Override
    protected EntityDAO<RenderedService> getDao() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    }
}
