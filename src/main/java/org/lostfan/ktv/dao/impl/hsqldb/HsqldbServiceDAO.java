package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;

import java.time.LocalDate;
import java.util.List;

public class HsqldbServiceDAO implements ServiceDAO {

    public List<Service> getAllServices() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Service getService(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void save(Service service) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void update(Service service) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void delete(Service service) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getCostByDay(Service service, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<ServicePrice> getServicePricesByServiceId(int serviceId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
