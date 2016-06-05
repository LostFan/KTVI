package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.RenderedServiceSearcherModel;
import org.lostfan.ktv.validation.SubscriptionFeeValidator;
import org.lostfan.ktv.validation.Validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubscriptionFeeModel extends BaseDocumentModel<RenderedService> {

    private LocalDate date;
    private List<RenderedService> entities;
    private List<EntityField> fields;
    private FullEntityField loadFullEntityField;

    private Validator<RenderedService> validator = new SubscriptionFeeValidator();

    public SubscriptionFeeModel() {

        date = LocalDate.now().withDayOfMonth(1);

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("renderedService.id", EntityFieldTypes.Integer, RenderedService::getId, RenderedService::setId, false));
        this.fields.add(new EntityField("renderedService.date", EntityFieldTypes.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, RenderedService::getSubscriberAccount, RenderedService::setSubscriberAccount));
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Integer, RenderedService::getPrice, RenderedService::setPrice));

        loadFullEntityField = new FullEntityField("renderedService", EntityFieldTypes.RenderedService, null, null, RenderedService::new);
        loadFullEntityField.setEntityFields(getFields().stream().filter(e -> !e.getTitleKey().equals("renderedService.id")).collect(Collectors.toList()));
    }

    public List<RenderedService> getList() {
        if (this.entities == null) {
            this.entities = getAll();
        }
        return this.entities;
    }

    @Override
    public String getEntityName() {
        return "subscriptionFee";
    }

    @Override
    public RenderedService createNewEntity() {
        RenderedService renderedService = new RenderedService();
        renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
        return renderedService;
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public EntitySearcherModel<RenderedService> createSearchModel() {
        return new RenderedServiceSearcherModel();
    }

    @Override
    public Validator<RenderedService> getValidator() {
        return validator;
    }

    public List<EntityField> getFields() {
        return fields;
    }

    public String getEntityNameKey() {
        return "subscriptionFee";
    }

    protected EntityDAO<RenderedService> getDao() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    }

    public Class getEntityClass() {
        return RenderedService.class;
    }

    protected void updateEntitiesList() {
        this.entities = getAll();
        if (this.searchCriteria != null && this.searchCriteria.size() > 0) {
            Stream<RenderedService> stream = this.entities.stream();
            for (FieldSearchCriterion<RenderedService> fieldSearchCriterion : this.searchCriteria) {
                stream = stream.filter(fieldSearchCriterion.buildPredicate());
            }
            this.entities = stream.collect(Collectors.toList());
        }
        this.notifyObservers(null);
    }

    public void setDate(LocalDate date) {
        this.date = date;
        updateEntitiesList();
    }

    public List<RenderedService> getAll() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getByMonth(this.date)
                .stream().filter(e -> e.getServiceId() == FixedServices.SUBSCRIPTION_FEE.getId()).collect(Collectors.toList());
    }

    public LocalDate getDate() {
        return this.date;
    }

    public FullEntityField getLoadFullEntityField() {
        return loadFullEntityField;
    }

    public void deleteAllRenderedServicesByMonth(LocalDate date) {
        List<RenderedService> renderedServices = ((RenderedServiceDAO) getDao()).getAllForMonth(FixedServices.SUBSCRIPTION_FEE.getId(), date);
        for (RenderedService renderedService : renderedServices) {
            getDao().delete(renderedService.getId());
        }
    }
    public void deleteRenderedServicesByMonthAndSubscriberId(LocalDate date, Integer subscriberId) {
        List<RenderedService> renderedServices = ((RenderedServiceDAO) getDao()).getAllForMonth(FixedServices.SUBSCRIPTION_FEE.getId(), subscriberId, date);
        for (RenderedService renderedService : renderedServices) {
            getDao().delete(renderedService.getId());
        }
    }

    public void saveAll(List<RenderedService> renderedServices) {
        getDao().transactionBegin();
        for (RenderedService renderedService : renderedServices) {
            if (renderedService.getId() == null) {
                getDao().save(renderedService);
            } else {
                getDao().update(renderedService);
            }
        }
        getDao().commit();
        updateEntitiesList();
    }
}
