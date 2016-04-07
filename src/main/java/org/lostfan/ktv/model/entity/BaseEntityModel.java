package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.validation.ValidationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseEntityModel<T extends Entity> extends BaseObservable implements EntityModel<T> {

    protected List<FieldSearchCriterion<T>> searchCriteria;

    protected List<T> entities;

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
        updateEntitiesList();
    }

    @Override
    public List<T> getList() {
        if (this.entities == null) {
            this.entities = getAll();
        }

        return this.entities;
    }

    @Override
    public List<FullEntityField> getFullFields() {
        return Collections.emptyList();
    }

    @Override
    public T getEntity(int id) {
        return getDao().get(id);
    }

    @Override
    public ValidationResult save(T entity) {
        ValidationResult result = this.getValidator().validate(entity);

        if (result.hasErrors()) {
            return result;
        }
        if (entity.getId() == null) {
            getDao().save(entity);
        } else {
            getDao().update(entity);
        }
        updateEntitiesList();
        return result;
    }

    @Override
    public ValidationResult update(T entity) {
        return save(entity);
    }

    @Override
    public ValidationResult deleteEntityById(Integer id) {
        getDao().delete(id);
        updateEntitiesList();
        return null;
    }

    @Override
    public ValidationResult deleteEntityById(List<Integer> ids) {
        if (ids.size() == 0) {
            return null;
        }

        for (Integer id : ids) {
            getDao().delete(id);
        }
        updateEntitiesList();
        return null;
    }


    protected void updateEntitiesList() {
        this.entities = getAll();
        if (this.searchCriteria != null && this.searchCriteria.size() > 0) {
            Stream<T> stream = this.entities.stream();
            for (FieldSearchCriterion<T> fieldSearchCriterion : this.searchCriteria) {
                stream = stream.filter(fieldSearchCriterion.buildPredicate());
            }
            this.entities = stream.collect(Collectors.toList());
        }
        this.notifyObservers(null);
    }

    protected List<T> getAll() {
        return getDao().getAll();
    }

    protected abstract EntityDAO<T> getDao();
}
