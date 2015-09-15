package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.domain.RenderedService;

import java.time.LocalDate;
import java.util.List;

public class HsqldbRenderedServiceDAO implements RenderedServiceDAO {

    public List<RenderedService> getAllRenderedServices() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<RenderedService> getRenderedServicesByDate(LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<RenderedService> getRenderedServicesBySubscriberId(int subscriberId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void save(RenderedService renderedService) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void update(RenderedService renderedService) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void delete(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
