package org.lostfan.ktv.model;

import org.lostfan.ktv.utils.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class BaseEntityModel<T> extends Observable implements EntityModel<T> {

    private List<FieldSearchCriterion<T>> searchCriteria;

    protected Function<T, Object> getter;

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

    public Object getId(T entity) {
        return getter.apply(entity);
    }

}
