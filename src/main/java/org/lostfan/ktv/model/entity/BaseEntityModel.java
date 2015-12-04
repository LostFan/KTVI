package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseEntityModel<T extends Entity> extends BaseObservable implements EntityModel<T> {

    protected List<FieldSearchCriterion<T>> searchCriteria;

    protected List<T> entities;

    private EntityModel parentModel;

    protected List<EntityModel> entityTableModels;

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
            this.entities = getDao().getAll();
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
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            getDao().delete(id);
        }
        updateEntitiesList();
    }

    @Override
    public void deleteEntityById(Integer id) {
        getDao().delete(id);
        updateEntitiesList();
    }


    protected void updateEntitiesList() {
        this.entities = getDao().getAll();
        if (this.searchCriteria != null && this.searchCriteria.size() > 0) {
            Stream<T> stream = this.entities.stream();
            for (FieldSearchCriterion<T> fieldSearchCriterion : this.searchCriteria) {
                stream = stream.filter(fieldSearchCriterion.buildPredicate());
            }
            this.entities = stream.collect(Collectors.toList());
        }
        this.notifyObservers(null);
    }

    protected abstract EntityDAO<T> getDao();

    /**
     * Dummy Implementation
     * TODO: Remove this method
     * when all the derived implementation classes acquire its own validators
     */
    @Override
    public Validator<T> getValidator() {
        return (entity, result) -> result;
    }
}
