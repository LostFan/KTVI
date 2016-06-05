package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.validation.PeriodValidator;
import org.lostfan.ktv.validation.SubscriptionFeeValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionFeeRecalculationModel extends BaseObservable {

    private Integer progress;
    private List<RenderedService> newRenderedServices;

    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private TariffDAO tariffDAO = DAOFactory.getDefaultDAOFactory().getTariffDAO();

    PeriodValidator periodValidator = new PeriodValidator();

    private Validator<RenderedService> validator = new SubscriptionFeeValidator();

    public SubscriptionFeeRecalculationModel() {
        this.progress = 0;
        this.newRenderedServices = new ArrayList<>();
    }

    public ValidationResult createSubscriptionFeeBySubscriber(Integer subscriberId, LocalDate date) {
        ValidationResult result = ValidationResult.createEmpty();
        result = periodValidator.validate(date, result);
        if(result.hasErrors()) {
            return result;
        }
        LocalDate beginDate = date.withDayOfMonth(1);
        newRenderedServices.add(getSubscriptionFeeByMonth(subscriberId, beginDate));
        return result;
    }


    public ValidationResult createSubscriptionFees(LocalDate date) {
        ValidationResult result = ValidationResult.createEmpty();
        result = periodValidator.validate(date, result);
        if(result.hasErrors()) {
            return result;
        }
        LocalDate beginDate = date.withDayOfMonth(1);
        createSubscriptionFeesInMonth(beginDate);
        return result;
    }

    private void createSubscriptionFeesInMonth(LocalDate date) {
        newRenderedServices.clear();
        List<Subscriber> subscribers = subscriberDAO.getAll();
        Integer count = 0;
        for (Subscriber subscriber : subscribers) {
            progress = 100 * count++ / subscribers.size();
            notifyObservers(null);
            RenderedService renderedService = getSubscriptionFeeByMonth(subscriber.getId(), date);
            if(renderedService != null) {
                newRenderedServices.add(getSubscriptionFeeByMonth(subscriber.getId(), date));
            }
        }
        progress = 100;
        notifyObservers(null);
    }

    protected EntityDAO<RenderedService> getDao() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    }

    private RenderedService getSubscriptionFeeByMonth(Integer subscriberId, LocalDate date) {
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

    public Integer getProgress() {
        return progress;
    }

    public List<RenderedService> getNewRenderedServices() {
        return newRenderedServices;
    }
}
