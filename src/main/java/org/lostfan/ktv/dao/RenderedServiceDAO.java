package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

import org.lostfan.ktv.domain.RenderedService;

public interface RenderedServiceDAO extends EntityDAO<RenderedService> {

    List<RenderedService> getByMonth(LocalDate date);

    List<RenderedService> getByDate(LocalDate date);

    List<RenderedService> getBySubscriber(int subscriberAccount);

    List<RenderedService> getByService(int serviceId);

    List<RenderedService> getAll(int subscriberAccount, int serviceId);

    List<RenderedService> getAll(int serviceId, LocalDate date);

    List<RenderedService> getAllForMonth(int serviceId, LocalDate date);

    List<RenderedService> getAllForMonth(int serviceId, int subscriberAccount, LocalDate date);

    Map<Integer, Integer> getAllRenderedServicesPriceInMonthForSubscriber(LocalDate date);

    Map<Integer, Integer> getAllRenderedServicesPriceInMonthForSubscriberByServiceId(int serviceId, LocalDate date);

    Map<Integer, Integer> getAllRenderedServicesPriceForSubscriberByServiceIdBeforeDate(int serviceId, LocalDate date);

    RenderedService getFirstRenderedServiceLessDate(int serviceId, int subscriberAccount, LocalDate date);

    Map<Integer, List<RenderedService>> getServiceAndSubscriberRenderedServiceMap();
}
