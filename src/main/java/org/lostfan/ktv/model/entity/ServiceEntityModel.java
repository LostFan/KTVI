package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class ServiceEntityModel extends BaseEntityModel<Service> {

    private List<EntityField> fields;

    public ServiceEntityModel() {

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("service.id", EntityFieldTypes.Integer, Service::getId, Service::setId, false));
        this.fields.add(new EntityField("service.name", EntityFieldTypes.String, Service::getName, Service::setName));
        this.fields.add(new EntityField("service.additional", EntityFieldTypes.Boolean, Service::isAdditionalService, Service::setAdditionalService));
        this.fields.add(new EntityField("service.change_tariff", EntityFieldTypes.Boolean, Service::isChangeTariff, Service::setChangeTariff));
        this.fields.add(new EntityField("service.consume_materials", EntityFieldTypes.Boolean, Service::isConsumeMaterials, Service::setConsumeMaterials));
        this.fields.add(new EntityField("service.connection_service", EntityFieldTypes.Boolean, Service::isConnectionService, Service::setConnectionService));
        this.fields.add(new EntityField("service.disconnection_service", EntityFieldTypes.Boolean, Service::isDisconnectionService, Service::setDisconnectionService));
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "services";
    }

    @Override
    public String getEntityName() {
        return "service";
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }

    @Override
    protected EntityDAO<Service> getDao() {
        return DAOFactory.getDefaultDAOFactory().getServiceDAO();
    }

    @Override
    public Service createNewEntity() {
        return new Service();
    }
}
