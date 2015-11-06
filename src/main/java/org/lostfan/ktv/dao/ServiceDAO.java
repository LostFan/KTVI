package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;

public interface ServiceDAO extends EntityDAO<Service> {

    List<Service> getAll();

    Service get(int id);

    void save(Service service);

    void update(Service service);

    void delete(int serviceId);

    int getPriceByDate(int serviceId, LocalDate date);

    List<ServicePrice> getServicePricesByServiceId(int serviceId);

    List<Service> getServicesByBeginningPartOfName(String str);
}
