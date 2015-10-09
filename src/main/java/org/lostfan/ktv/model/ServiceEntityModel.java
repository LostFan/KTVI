package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceEntityModel extends BaseEntityModel<Service> {

    private List<EntityField<Service, ?>> fields;

    private ServiceDAO dao;
    private List<Service> services;

    public ServiceEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<Service, Integer>("service.id", EntityField.Types.Integer, Service::getId, Service::setId));
        this.fields.add(new EntityField<>("service.name", EntityField.Types.String, Service::getName, Service::setName));
        this.fields.add(new EntityField<>("service.additional", EntityField.Types.Boolean, Service::isAdditionalService, Service::setAdditionalService));
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
    public String getEntityNameKey() {
        return "services";
    }

    @Override
    public FieldsComboBoxModel<Service> getFieldComboBoxModel() {
        return new FieldsComboBoxModel<>(fields);
    }

    @Override
    public String getEntityName() {
        return "service";
    }

    @Override
    public void saveOrEditEntity(List<FieldValue<Service>> fieldValues) {
        Map<String, FieldValue>  collect =  fieldValues.stream().collect(Collectors.toMap(
                criterion -> criterion.getFieldName(),
                Function.identity()));
        Service service = new Service();
        service.setName(collect.get("service.name").getValue().toString());
        service.setAdditionalService((Boolean) collect.get("service.additional").getValue());
        if(collect.get("service.id") != null) {
            Integer serviceId = (Integer) collect.get("service.id").getValue();
            System.out.println(serviceId);

            if (this.dao.getService(serviceId) != null) {
                service.setId((Integer) collect.get("service.id").getValue());
                this.dao.update(service);
            } else {
            this.dao.save(service);
            }
        }
        this.services = this.dao.getAllServices();
        this.notifyObservers(null);
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
