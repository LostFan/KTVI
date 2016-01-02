package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class AdditionalServiceSearcherModel extends EntitySearcherModel<Service> {

    private List<EntityField> fields;

    public AdditionalServiceSearcherModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("service.id", EntityFieldTypes.Integer, Service::getId, Service::setId, false));
        this.fields.add(new EntityField("service.name", EntityFieldTypes.String, Service::getName, Service::setName));
        this.fields.add(new EntityField("service.additional", EntityFieldTypes.Boolean, Service::isAdditionalService, Service::setAdditionalService));
    }

    @Override
    public List<Service> getList() {
        if (this.entities == null) {
            this.entities = getDao().getAll().stream().filter(e->e.isAdditionalService()).collect(Collectors.toList());
        }

        return this.entities;
    }

    @Override
    public void setSearchQuery(String query) {
        this.entities = getDao().getAllContainsInName(query).stream().filter(e->e.isAdditionalService()).collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return RenderedService.class;
    }

    @Override
    public String getEntityNameKey() {
        return "additionalService";
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