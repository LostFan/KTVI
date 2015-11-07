package org.lostfan.ktv.dao;


import org.lostfan.ktv.domain.Street;

import java.util.List;


public interface StreetDAO extends EntityDAO<Street> {

    List<Street> getAll();

    Street get(int id);

    void save(Street subscriber);

    void update(Street subscriber);

    void delete(int subscriberId);

    List<Street> getStreetsByBeginningPartOfName(String str);
}
