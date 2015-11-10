package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

public class ServiceEntityModel extends BaseEntityModel<Service> {

    private List<EntityField> fields;

    private List<Service> services;

    public ServiceEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("service.id", EntityFieldTypes.Integer, Service::getId, Service::setId));
        this.fields.add(new EntityField("service.name", EntityFieldTypes.String, Service::getName, Service::setName));
        this.fields.add(new EntityField("service.additional", EntityFieldTypes.Boolean, Service::isAdditionalService, Service::setAdditionalService));
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    public List<Service> getList() {
        if (this.services == null) {
            this.services = this.dao.getAll();
        }

        return this.services;
    }

    @Override
    public List<Service> getListByForeignKey(int foreignKey) {
        return null;
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
    public void saveOrEditEntity(Map<String, Object> collect) {

        Service service = new Service();
        service.setName(collect.get("service.name").toString());
        service.setAdditionalService((Boolean) collect.get("service.additional"));

        if(collect.get("service.id") != null) {
            Integer serviceId = (Integer) collect.get("service.id");
            service.setId((Integer) collect.get("service.id"));
            if (this.dao.get(serviceId) != null) {
                this.dao.update(service);
            } else {
                this.dao.save(service);
            }
        } else {
            this.dao.save(service);
        }
        this.services = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.services = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Service>> criteria) {
        super.setSearchCriteria(criteria);

        this.services = this.dao.getAll();
        Stream<Service> stream = this.services.stream();
        for (FieldSearchCriterion<Service> serviceFieldSearchCriterion : criteria) {
            stream = stream.filter(serviceFieldSearchCriterion.buildPredicate());
        }

        this.services = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }
}
