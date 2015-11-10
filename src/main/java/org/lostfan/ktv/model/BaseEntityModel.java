package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.utils.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class BaseEntityModel<T extends Entity> extends BaseObservable implements EntityModel<T> {

    private List<FieldSearchCriterion<T>> searchCriteria;

    protected EntityDAO<T> dao;

    public BaseEntityModel() {
        this.searchCriteria = new ArrayList<>();
    }

    @Override
    public List<FieldSearchCriterion<T>> getSearchCriteria() {
        return this.searchCriteria;
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<T>> criteria) {
        this.searchCriteria = criteria;
    }

    @Override
    public List<EntityModel> getTableModels() {
        return null;
    }

    @Override
    public T getEntity(int id) {
        return dao != null ? dao.get(id) : null;
    }

}
