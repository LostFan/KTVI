package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.RenderedServiceSearcherModel;
import org.lostfan.ktv.validation.SubscriptionFeeValidator;
import org.lostfan.ktv.validation.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Double, RenderedService::getPrice, RenderedService::setPrice));

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

    public void generateAllSubscriptionFees(List<RenderedService> renderedServices) {

        if(renderedServices.size() == 0) {
            return;
        }
        List<RenderedService> list =((RenderedServiceDAO) getDao()).getAllForMonth(renderedServices.get(0).getServiceId(),
                renderedServices.get(0).getDate());
        list = list.stream().filter(e -> e.getDate().equals(renderedServices.get(0).getDate().withDayOfMonth(1))).collect(Collectors.toList());
        Map<Integer, List<RenderedService>> map = new HashMap<>();
        for (RenderedService renderedService : list) {
            List<RenderedService> value = map.get(renderedService.getSubscriberAccount());
            if(value == null) {
                value = new ArrayList<>();
                map.put(renderedService.getSubscriberAccount(), value);
            }
            value.add(renderedService);
        }

        getDao().transactionBegin();

        for (RenderedService renderedService : renderedServices) {
            list = map.get(renderedService.getSubscriberAccount());
            map.remove(renderedService.getSubscriberAccount());
            if(list == null || list.isEmpty()) {
                getDao().save(renderedService);
                continue;
            }
            if(list.size() > 1) {
                continue;
            }
            for (RenderedService monthService : list) {
                if(monthService.getDate().equals(renderedService.getDate())) {
                    renderedService.setId(monthService.getId());
                    getDao().update(renderedService);
                }
            }
        }

        for (Map.Entry<Integer, List<RenderedService>> integerListEntry : map.entrySet()) {
            for (RenderedService renderedService : integerListEntry.getValue()) {
                getDao().delete(renderedService.getId());
            }
        }
        getDao().commit();
        updateEntitiesList();
    }

    public void generateSeveralSubscriptionFees(List<RenderedService> renderedServices) {
        if(renderedServices.size() == 0) {
            return;
        }
        List<RenderedService> list =((RenderedServiceDAO) getDao()).getAllForMonth(renderedServices.get(0).getServiceId(),
                renderedServices.get(0).getDate());
        Map<Integer, List<RenderedService>> map = new HashMap<>();
        for (RenderedService renderedService : list) {
            List<RenderedService> value = map.get(renderedService.getSubscriberAccount());
            if(value == null) {
                value = new ArrayList<>();
                map.put(renderedService.getSubscriberAccount(), value);
            }
            value.add(renderedService);
        }

        getDao().transactionBegin();

        for (RenderedService renderedService : renderedServices) {
            list = map.get(renderedService.getSubscriberAccount());
            if(list == null || list.isEmpty()) {
                getDao().save(renderedService);
                continue;
            }
            if(list.size() > 1) {
                continue;
            }
            for (RenderedService monthService : list) {
                if(monthService.getDate().equals(renderedService.getDate())) {
                    renderedService.setId(monthService.getId());
                    getDao().update(renderedService);
                }
            }
        }

        getDao().commit();
        updateEntitiesList();

    }
}
