package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;

public interface ServiceDAO {

    public List<Service> getAllServices();

    public Service getService(int id);

    public void save(Service service);

    public void update(Service service);

    public void delete(int serviceId);

    public int getPriceByDay(int serviceId, LocalDate date);

    public  List<ServicePrice> getServicePricesByServiceId(int serviceId);
}
