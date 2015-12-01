package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.ServicePrice;

public interface ServicePriceDAO extends EntityDAO<ServicePrice> {

    List<ServicePrice> getAll();

    ServicePrice get(int id);

    void save(ServicePrice service);

    void update(ServicePrice service);

    void delete(int serviceId);

    List<ServicePrice> getAllContainsInName(String str);

    Integer getPriceByDate(int serviceId, LocalDate date);

    ServicePrice getByServiceIdAndDate(int serviceId, LocalDate date);

    void deleteByServiceIdAndDate(int serviceId, LocalDate date);

    List<ServicePrice> getServicePricesByServiceId(int serviceId);
}
