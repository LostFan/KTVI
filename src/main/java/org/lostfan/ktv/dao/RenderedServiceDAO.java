package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.RenderedService;

public interface RenderedServiceDAO {

    public List<RenderedService> getAllRenderedServices();

    public List<RenderedService> getRenderedServicesByDate(LocalDate date);

    public void save(RenderedService renderedService);

    public void update(RenderedService renderedService);

    public void delete(RenderedService renderedService);
}
