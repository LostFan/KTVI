package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceModel extends BaseModel<Service> {

    private List<EntityField<Service, ?>> fields;

    private ServiceDAO dao;
    private List<Service> services;

    public ServiceModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("ID", EntityField.Types.Integer, Service::getId, Service::setId));
        this.fields.add(new EntityField<>("Name", EntityField.Types.String, Service::getName, Service::setName));
        this.fields.add(new EntityField<>("Additional", EntityField.Types.Boolean, Service::isAdditionalService, Service::setAdditionalService));
    }

    @Override
    public List<EntityField<Service, ?>> getFields() {
        return this.fields;
    }

    public List<Service> getList() {
        if (this.services == null) {
            this.services = this.dao.getAllServices();
        }

        return this.services;
    }

    public TableModel getTableModel() {
        return new EntityTableModel<>(this);
    }

    @Override
    public String getEntityName() {
        return "Услуги";
    }

    @Override
    public FieldsComboBoxModel<Service> getFieldComboBoxModel() {
        return new FieldsComboBoxModel<>(fields);
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Service>> criteria) {
        super.setSearchCriteria(criteria);

        this.services = this.dao.getAllServices();
        Stream<Service> stream = this.services.stream();
        for (FieldSearchCriterion<Service> serviceFieldSearchCriterion : criteria) {
            stream = stream.filter(serviceFieldSearchCriterion.buildPredicate());
        }

        this.services = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }
}
