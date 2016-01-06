package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.RenderedService;

public interface RenderedServiceDAO extends EntityDAO<RenderedService> {

    List<RenderedService> getRenderedServicesByDate(LocalDate date);

    List<RenderedService> getRenderedServicesBySubscriberId(int subscriberId);

    List<RenderedService> getRenderedServicesByServiceId(int serviceId);

    List<RenderedService> getRenderedServicesByServiceIdAndDate(int serviceId, LocalDate date);

    List<RenderedService> getRenderedServicesByServiceIdInMonth(int serviceId, LocalDate date);

    List<RenderedService> getRenderedServicesByServiceIdAndSubscriberIdInMonth(int serviceId, int subscriberId, LocalDate date);

    RenderedService getFirstRenderedServiceLessDate(int serviceId, int subscriberId, LocalDate date);
}
