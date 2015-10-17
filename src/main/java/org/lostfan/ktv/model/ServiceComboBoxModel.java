package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

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


    public ServiceComboBoxModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("service.id", EntityField.Types.Integer, Service::getId, Service::setId));
        this.fields.add(new EntityField<>("service.name", EntityField.Types.String, Service::getName, Service::setName));
    }


    @Override
    public List<Service> getListByBeginningPartOfName(String str) {
        this.services = dao.getServicesByBeginningPartOfName(str);
        this.notifyObservers(null);
        return this.services;
    }

    @Override
    public List<Service> getList() {
        if (this.services == null) {
            this.services = this.dao.getAllServices();
        }
        return this.services;
    }

    @Override
    public List<EntityField<Service, ?>> getFields() {
        return this.fields;
    }

    public ComboBoxModel getTableModel() {
        return new ValueComboBoxModel<>(this);
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }

    @Override
    public String getNameById(int id) {
        return this.dao.getService(id).getName();
    }
}
