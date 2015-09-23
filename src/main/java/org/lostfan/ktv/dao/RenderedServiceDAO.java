package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.RenderedService;

public interface RenderedServiceDAO {

    List<RenderedService> getAllRenderedServices();

    RenderedService getRenderedService(int id);

    List<RenderedService> getRenderedServicesByDate(LocalDate date);

    List<RenderedService> getRenderedServicesBySubscriberId(int subscriberId);

    void save(RenderedService renderedService);

    void update(RenderedService renderedService);

    void delete(int id);
}
