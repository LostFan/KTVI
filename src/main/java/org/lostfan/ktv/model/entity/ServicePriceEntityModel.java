package org.lostfan.ktv.model.entity;


import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class ServicePriceEntityModel extends BaseEntityModel<ServicePrice> {

    private List<EntityField> fields;

    public ServicePriceEntityModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("servicePrice.id", EntityFieldTypes.Integer, ServicePrice::getId, ServicePrice::setId, false));
        this.fields.add(new EntityField("servicePrice.date", EntityFieldTypes.Date, ServicePrice::getDate, ServicePrice::setDate));
        this.fields.add(new EntityField("service", EntityFieldTypes.Service, ServicePrice::getServiceId, ServicePrice::setServiceId));
        this.fields.add(new EntityField("servicePrice.price", EntityFieldTypes.Integer, ServicePrice::getPrice, ServicePrice::setPrice));
    }

    @Override
    protected EntityDAO<ServicePrice> getDao() {
        return DAOFactory.getDefaultDAOFactory().getServicePriceDAO();
    }

    @Override
    public List<ServicePrice> getListByForeignKey(Integer foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "servicePrice";
    }

    @Override
    public String getEntityName() {
        return "servicePrice";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public ServicePrice createNewEntity() {
        return new ServicePrice();
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return ServicePrice.class;
    }
}
