package org.lostfan.ktv.model;

import org.lostfan.ktv.utils.Observable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel<T> extends Observable implements Model<T> {

    private List<FieldSearchCriterion<T>> searchCriteria;

    public BaseModel() {
        this.searchCriteria = new ArrayList<>();
    }

    @Override
    public FieldsComboBoxModel<T> getFieldComboBoxModel() {
        return new FieldsComboBoxModel<>(this.getFields());
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
