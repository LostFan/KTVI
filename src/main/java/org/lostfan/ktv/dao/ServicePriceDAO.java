package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;

public interface ServicePriceDAO {

    public List<ServicePrice> getAllServicePrices();

    public  List<ServicePrice> getServicePricesByService(Service service);

    public  List<ServicePrice> getServicePricesByDate(LocalDate date);

    public void save(ServicePrice servicePrice);

    public void update(ServicePrice servicePrice);

    public void delete(ServicePrice servicePrice);
}
