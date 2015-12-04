package org.lostfan.ktv.dao;


import org.lostfan.ktv.domain.Street;

import java.util.List;


public interface StreetDAO extends EntityDAO<Street> {

    List<Street> getStreetsByBeginningPartOfName(String str);
}
