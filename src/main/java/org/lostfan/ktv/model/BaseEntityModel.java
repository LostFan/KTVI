package org.lostfan.ktv.model;

import org.lostfan.ktv.utils.Observable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEntityModel<T> extends Observable implements EntityModel<T> {

    private List<FieldSearchCriterion<T>> searchCriteria;

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
}
