package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class ServiceSearcherModel extends EntitySearcherModel<Service> {

    private List<EntityField> fields;

    public ServiceSearcherModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("service.id", EntityFieldTypes.Integer, Service::getId, Service::setId, false));
        this.fields.add(new EntityField("service.name", EntityFieldTypes.String, Service::getName, Service::setName));
        this.fields.add(new EntityField("service.additional", EntityFieldTypes.Boolean, Service::isAdditionalService, Service::setAdditionalService));
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }

    @Override
    public String getEntityNameKey() {
        return "services";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<Service> getDao() {
        return DAOFactory.getDefaultDAOFactory().getServiceDAO();
    }
}
