package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.RenderedService;

public interface RenderedServiceDAO extends EntityDAO<RenderedService> {

    List<RenderedService> getAll();

    RenderedService get(int id);

    List<RenderedService> getRenderedServicesByDate(LocalDate date);

    List<RenderedService> getRenderedServicesBySubscriberId(int subscriberId);

    RenderedService save(RenderedService renderedService);

    RenderedService update(RenderedService renderedService);

    void delete(int id);

    List<RenderedService> getAllContainsInName(String str);
}
