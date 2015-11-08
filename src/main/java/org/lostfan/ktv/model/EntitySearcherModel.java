package org.lostfan.ktv.model;

import java.util.List;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.utils.Observable;

public abstract class EntitySearcherModel<T extends Entity> extends Observable {

    public abstract  List<T> getList();

    public abstract void setSearchQuery(String query);

    public abstract Class getEntityClass();
}
