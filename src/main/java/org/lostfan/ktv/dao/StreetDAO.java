package org.lostfan.ktv.dao;


import org.lostfan.ktv.domain.Street;

import java.util.List;


public interface StreetDAO extends EntityDAO<Street> {

    List<Street> getAll();

    Street get(int id);

    Street save(Street subscriber);

    Street update(Street subscriber);

    void delete(int subscriberId);

    List<Street> getStreetsByBeginningPartOfName(String str);

    List<Street> getAllContainsInName(String str);
}
