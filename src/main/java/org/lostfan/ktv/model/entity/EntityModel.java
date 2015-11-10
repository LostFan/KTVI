package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.utils.Observable;
import org.lostfan.ktv.validation.Validator;

import java.util.List;

public interface EntityModel<T extends Entity> extends Observable {

    List<T> getList();

    List<T> getListByForeignKey(int foreignKey);

    T getEntity(int id);

    String getEntityNameKey();

    String getEntityName();

    List<EntityField> getFields();

    List<FieldSearchCriterion<T>> getSearchCriteria();

    void setSearchCriteria(List<FieldSearchCriterion<T>> criteria);

    void save(T entity);

    T createNewEntity();

    void deleteEntityByRow(List<Integer> rowNumbers);

    List<EntityModel> getEntityModels();

    Class getEntityClass();

    List<EntityModel> getTableModels();

    Validator<T> getValidator();

}
