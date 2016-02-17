package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Document;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.validation.PeriodValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

public abstract class BaseDocumentModel<T extends Document> extends BaseObservable implements EntityModel<T> {

    protected List<FieldSearchCriterion<T>> searchCriteria;

    protected List<T> entities;

    protected PeriodValidator periodValidator = new PeriodValidator();

    public BaseDocumentModel() {
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

        periodValidator.validate(entity, result);

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
        T document = getDao().get(id);
        ValidationResult result = periodValidator.validate(document);
        if(result.hasErrors()) {
            return result;
        }
        getDao().delete(id);
        updateEntitiesList();
        return result;
    }

    @Override
    public ValidationResult deleteEntityById(List<Integer> ids) {
        ValidationResult result = ValidationResult.createEmpty();
        if (ids.size() == 0) {
            return result;
        }

        for (Integer id : ids) {
            T document = getDao().get(id);
            result = periodValidator.validate(document);
        }

        if(result.hasErrors()) {
            return result;
        }
        for (Integer id : ids) {
            getDao().delete(id);
        }
        updateEntitiesList();
        return result;
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

    /**
     * Dummy Implementation
     * TODO: Remove this method
     * when all the derived implementation classes acquire its own validators
     */
    @Override
    public Validator<T> getValidator() {
        return (entity, result) -> result;
    }

    public PeriodValidator getPeriodValidator() {
        return periodValidator;
    }
}
