package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class ServiceComboBoxModel extends EntityComboBoxModel<Service> {

    private ServiceDAO dao;
    private List<Service> services;
    private List<EntityField<Service, ?>> fields;
    private EntityField entityFieldId;
    private EntityField entityFieldName;


    public ServiceComboBoxModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();

        this.fields = new ArrayList<>();
        entityFieldId = new EntityField<>("service.id", Types.Integer, Service::getId, Service::setId);
        entityFieldName = new EntityField<>("service.name", Types.String, Service::getName, Service::setName);
    }


    @Override
    public void setListByBeginningPartOfName(String str) {
        this.services = dao.getServicesByBeginningPartOfName(str);
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
    public EntityField<Service, ?> getEntityFieldName() {
        return entityFieldName;
    }

    @Override
    public EntityField<Service, ?> getEntityFieldId() {
        return entityFieldId;
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }
}
