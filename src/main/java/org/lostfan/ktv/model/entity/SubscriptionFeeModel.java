package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.utils.BaseObservable;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubscriptionFeeModel extends BaseObservable implements BaseModel {

    private LocalDate date;
    private List<FieldSearchCriterion> searchCriteria;
    private List<RenderedService> entities;
    private List<EntityField> fields;

    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private TariffDAO tariffDAO = DAOFactory.getDefaultDAOFactory().getTariffDAO();

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

    public void createSubscriptionFeeBySubscriber(Integer subscriberId, LocalDate date) {
        LocalDate beginDate = date.withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);
        while(beginDate.isBefore(endDate)){
            System.out.println(beginDate);
            createSubscriptionFeeInMouthBySubscriber(subscriberId, beginDate);
            beginDate = beginDate.plusMonths(1);
        }
        updateEntitiesList();
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

    public void createSubscriptionFees(LocalDate date) {
        LocalDate beginDate = date.withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);
        while(beginDate.isBefore(endDate)){
            System.out.println(beginDate);
            createSubscriptionFeesInMouth(beginDate);
            beginDate = beginDate.plusMonths(1);
        }
        updateEntitiesList();
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
        RenderedService renderedService = new RenderedService();
        renderedService.setDate(date);
        renderedService.setServiceId(FixedServices.SUBSCRIPTION_FEE.getId());
        renderedService.setSubscriberAccount(subscriberId);
        SubscriberTariff subscriberTariffAllMonth = subscriberDAO.getSubscriberTariffBySubscriberIdInAllMonth(subscriberId, date);
        if(subscriberTariffAllMonth != null) {
            Integer price = tariffDAO.getTariffPrice(subscriberTariffAllMonth.getTariffId(), date).getPrice();
            renderedService.setPrice(price);
            getDao().save(renderedService);
            return;
        }
        Integer allPrice = 0;
        SubscriberTariff subscriberTariffBeginOfMonth = subscriberDAO.getSubscriberTariffBySubscriberIdInMonthBeginInPrevMonthEndInCurrentMonth(subscriberId, date);
        if(subscriberTariffBeginOfMonth != null) {
            int days = Period.between(date, subscriberTariffBeginOfMonth.getDisconnectTariff()).getDays();
            Integer tariffPrice = tariffDAO.getTariffPrice(subscriberTariffBeginOfMonth.getTariffId(), date).getPrice();
            Integer price = rounding(tariffPrice * days / date.getMonth().length(date.isLeapYear()));
            allPrice += price;
        }
        List<SubscriberTariff> subscriberTariffs = subscriberDAO.getSubscriberTariffsBySubscriberIdInMonthBeginInCurrentMonthEndInCurrentMonth(subscriberId, date);
        for (SubscriberTariff subscriberTariff : subscriberTariffs) {
            int days = Period.between(subscriberTariff.getConnectTariff(), subscriberTariff.getDisconnectTariff()).getDays();
            Integer tariffPrice = tariffDAO.getTariffPrice(subscriberTariff.getTariffId(), date).getPrice();
            Integer price = rounding(tariffPrice * days / date.getMonth().length(date.isLeapYear()));
            allPrice += price;
        }
        SubscriberTariff subscriberTariffEndOfMonth = subscriberDAO.getSubscriberTariffBySubscriberIdInMonthBeginInCurrentMonth(subscriberId, date);
        if(subscriberTariffEndOfMonth != null) {
            renderedService.setDate(subscriberTariffEndOfMonth.getConnectTariff());
            int days = Period.between(subscriberTariffEndOfMonth.getConnectTariff(), date.plusMonths(1)).getDays();
            Integer tariffPrice = tariffDAO.getTariffPrice(subscriberTariffEndOfMonth.getTariffId(), date).getPrice();
            Integer price = rounding(tariffPrice * days / date.getMonth().length(date.isLeapYear()));
            allPrice += price;
        }
        renderedService.setPrice(allPrice);
        if(allPrice > 0) {
            getDao().save(renderedService);
        }
    }

    private Integer rounding(Integer number) {
        if(number == null) {
            return null;
        }
        if(number % 100 < 50) {
            return number / 100 * 100;
        } else {
            return (number + 50) / 100 * 100;
        }
    }

    public void setSearchCriteria(List<FieldSearchCriterion> criteria) {
        this.searchCriteria = criteria;
        updateEntitiesList();
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
