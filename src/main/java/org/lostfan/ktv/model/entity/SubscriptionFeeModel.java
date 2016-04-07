package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.RenderedServiceSearcherModel;
import org.lostfan.ktv.validation.SubscriptionFeeValidator;
import org.lostfan.ktv.validation.ValidationResult;
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

    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private TariffDAO tariffDAO = DAOFactory.getDefaultDAOFactory().getTariffDAO();

    private Validator<RenderedService> validator = new SubscriptionFeeValidator();

    public SubscriptionFeeModel() {

        date = LocalDate.now().withDayOfMonth(1);

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("renderedService.id", EntityFieldTypes.Integer, RenderedService::getId, RenderedService::setId, false));
        this.fields.add(new EntityField("renderedService.date", EntityFieldTypes.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, RenderedService::getSubscriberAccount, RenderedService::setSubscriberAccount));
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Integer, RenderedService::getPrice, RenderedService::setPrice));
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

    public ValidationResult createSubscriptionFeeBySubscriber(Integer subscriberId, LocalDate date) {
        ValidationResult result = ValidationResult.createEmpty();
        result = periodValidator.validate(date, result);
        if(result.hasErrors()) {
            return result;
        }
        LocalDate beginDate = date.withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);
        while(beginDate.isBefore(endDate)){
            System.out.println(beginDate);
            createSubscriptionFeeInMouthBySubscriber(subscriberId, beginDate);
            beginDate = beginDate.plusMonths(1);
        }
        updateEntitiesList();
        return result;
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

    public ValidationResult createSubscriptionFees(LocalDate date) {
        ValidationResult result = ValidationResult.createEmpty();
        result = periodValidator.validate(date, result);
        if(result.hasErrors()) {
            return result;
        }
        LocalDate beginDate = date.withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);
        while(beginDate.isBefore(endDate)){
            System.out.println(beginDate);
            createSubscriptionFeesInMouth(beginDate);
            beginDate = beginDate.plusMonths(1);
        }
        updateEntitiesList();
        return result;
    }

    private void createSubscriptionFeeInMouthBySubscriber(Integer subscriberId, LocalDate date) {
        List<RenderedService> renderedServices = ((RenderedServiceDAO) getDao()).getAllForMonth(FixedServices.SUBSCRIPTION_FEE.getId(), subscriberId, date);
        for (RenderedService renderedService : renderedServices) {
            getDao().delete(renderedService.getId());
        }
        saveRenderedService(subscriberId, date);
    }

    private void createSubscriptionFeesInMouth(LocalDate date) {
        List<Subscriber> subscribers = subscriberDAO.getAll();
        List<RenderedService> renderedServices = ((RenderedServiceDAO) getDao()).getAllForMonth(FixedServices.SUBSCRIPTION_FEE.getId(), date);
        for (RenderedService renderedService : renderedServices) {
            getDao().delete(renderedService.getId());
        }
        for (Subscriber subscriber : subscribers) {
            saveRenderedService(subscriber.getId(), date);
        }
    }

    private void saveRenderedService(Integer subscriberId, LocalDate date) {
        RenderedService renderedService = getSubscriptionFeeByMouth(subscriberId, date);
        if (renderedService != null) {
            getDao().save(renderedService);
        }
    }

    private RenderedService getSubscriptionFeeByMouth(Integer subscriberId, LocalDate date) {
        RenderedService renderedService = new RenderedService();
        renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
        renderedService.setSubscriberAccount(subscriberId);
        renderedService.setDate(date.withDayOfMonth(1));

        List<SubscriberSession> subscriberSessions = subscriberDAO.getSubscriberSessionsForMonth(subscriberId, date);
        Integer allPrice = 0;
        for (SubscriberSession subscriberSession : subscriberSessions) {
            LocalDate beginDate = date.withDayOfMonth(1).equals(subscriberSession.getConnectionDate().withDayOfMonth(1))
                    ? subscriberSession.getConnectionDate().plusDays(1) : date.withDayOfMonth(1);
            LocalDate endDate = subscriberSession.getDisconnectionDate() == null
                    || !date.withDayOfMonth(date.lengthOfMonth()).equals(subscriberSession.getDisconnectionDate().withDayOfMonth(subscriberSession.getDisconnectionDate().lengthOfMonth()))
                    ? date.withDayOfMonth(1).plusMonths(1) : subscriberSession.getDisconnectionDate();
            List<SubscriberTariff> subscriberTariffs = subscriberDAO.getSubscriberTariffsForInterval(subscriberId, beginDate, endDate);
            for (SubscriberTariff subscriberTariff : subscriberTariffs) {
                LocalDate beginTariffDate = date.withDayOfMonth(1).equals(subscriberTariff.getConnectTariff().withDayOfMonth(1))
                        ? subscriberTariff.getConnectTariff() : date.withDayOfMonth(1);
                beginTariffDate = beginTariffDate.isAfter(beginDate) ? beginTariffDate : beginDate;
                LocalDate endTariffDate = subscriberTariff.getDisconnectTariff() == null
                        || !date.withDayOfMonth(date.lengthOfMonth()).equals(subscriberTariff.getDisconnectTariff().withDayOfMonth(subscriberTariff.getDisconnectTariff().lengthOfMonth()))
                        ? date.withDayOfMonth(1).plusMonths(1) : subscriberTariff.getDisconnectTariff();
                endTariffDate = subscriberSession.getDisconnectionDate() == null ||
                        subscriberSession.getDisconnectionDate().isAfter(endTariffDate) ? endTariffDate
                        : subscriberSession.getDisconnectionDate();
                Integer months =  Period.between(beginTariffDate, endTariffDate).getMonths();
                if(months == 1) {
                    allPrice = tariffDAO.getPriceByDate(subscriberTariff.getTariffId(), beginTariffDate);
                    if(allPrice == 0) {
                        return null;
                    }
                    renderedService.setPrice(allPrice);
                    return renderedService;
                }
                Integer days =  Period.between(beginTariffDate, endTariffDate).getDays();
                if(days > 0) {
                    Integer price = rounding(tariffDAO.getPriceByDate(subscriberTariff.getTariffId(), beginTariffDate) / beginTariffDate.lengthOfMonth() * days);
                    allPrice += price;
                }
            }
        }
        if(allPrice == 0) {
            return null;
        }
        renderedService.setPrice(allPrice);

        return renderedService;
    }

    private Integer rounding(Integer number) {
        if(number == null) {
            return null;
        }
        return (number + 50) / 100 * 100;
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
}
