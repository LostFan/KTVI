package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.RenderedService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RenderedServiceEntityModel extends BaseEntityModel<RenderedService> {

    private List<EntityField<RenderedService, ?>> fields;

    private RenderedServiceDAO dao;
    private List<RenderedService> renderedServices;

    public RenderedServiceEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
        fields = new ArrayList<>();
        super.getter = RenderedService::getId;

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("renderedService.id", Types.Integer, RenderedService::getId, RenderedService::setId));
        this.fields.add(new EntityField<>("renderedService.date", Types.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField<>("subscriber", Types.Subscriber, RenderedService::getSubscriberId, RenderedService::setSubscriberId));
        this.fields.add(new EntityField<>("service", Types.Service, RenderedService::getServiceId, RenderedService::setServiceId));
        this.fields.add(new EntityField<>("renderedService.price", Types.Integer, RenderedService::getPrice, RenderedService::setPrice));
    }

    @Override
    public void saveOrEditEntity(Map<String, Object> collect) {
        RenderedService renderedService = new RenderedService();
        renderedService.setDate((LocalDate) collect.get("renderedService.date"));
        renderedService.setSubscriberId((Integer) collect.get("subscriber"));
        renderedService.setServiceId((Integer) collect.get("service"));
        renderedService.setPrice((Integer) collect.get("renderedService.price"));
        if(collect.get("renderedService.id") != null) {
            Integer renderedServiceId = (Integer) collect.get("renderedService.id");
            if (this.dao.getRenderedService(renderedServiceId) != null) {
                renderedService.setId((Integer) collect.get("renderedService.id"));
                this.dao.update(renderedService);
            } else {
                this.dao.save(renderedService);
            }
        } else {
            this.dao.save(renderedService);
        }
        this.renderedServices = this.dao.getAllRenderedServices();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.renderedServices = this.dao.getAllRenderedServices();
        this.notifyObservers(null);
    }

    @Override
    public List<EntityComboBoxModel> getEntityComboBoxModels() {
        List<EntityComboBoxModel> entityComboBoxModels = new ArrayList<>();
        entityComboBoxModels.add(new ServiceComboBoxModel());
        entityComboBoxModels.add(new SubscriberComboBoxModel());
        return entityComboBoxModels;
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(new ServiceEntityModel());
        entityModels.add(new SubscriberEntityModel());
        return entityModels;
    }

    @Override
    public String getEntityName() {
        return "renderedService";
    }

    @Override
    public List<EntityField<RenderedService, ?>> getFields() {
        return this.fields;
    }

    public List<RenderedService> getList() {
        if (this.renderedServices == null) {
            this.renderedServices = this.dao.getAllRenderedServices();
        }

        return this.renderedServices;
    }

    @Override
    public List<RenderedService> getListByForeignKey(int foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "renderedServices";
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<RenderedService>> criteria) {
        super.setSearchCriteria(criteria);

        this.renderedServices = this.dao.getAllRenderedServices();
        Stream<RenderedService> stream = this.renderedServices.stream();
        for (FieldSearchCriterion<RenderedService> renderedServiceFieldSearchCriterion : criteria) {
            stream = stream.filter(renderedServiceFieldSearchCriterion.buildPredicate());
        }

        this.renderedServices = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return RenderedService.class;
    }

    @Override
    public List<EntityModel> getTableModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(new MaterialConsumptionEntityModel());
        return entityModels;
    }
}
