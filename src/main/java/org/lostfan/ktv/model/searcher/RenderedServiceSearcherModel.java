package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class RenderedServiceSearcherModel extends EntitySearcherModel<RenderedService> {

    private List<EntityField> fields;

    public RenderedServiceSearcherModel() {

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("renderedService.id", EntityFieldTypes.Integer, RenderedService::getId, RenderedService::setId, false));
        this.fields.add(new EntityField("renderedService", EntityFieldTypes.Service, RenderedService::getServiceId, RenderedService::setServiceId, false));
        this.fields.add(new EntityField("renderedService.date", EntityFieldTypes.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, RenderedService::getSubscriberAccount, RenderedService::setSubscriberAccount));
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Integer, RenderedService::getPrice, RenderedService::setPrice));
    }

    @Override
    public Class getEntityClass() {
        return RenderedService.class;
    }

    @Override
    public String getEntityNameKey() {
        return "renderedServices";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<RenderedService> getDao() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    }
}
