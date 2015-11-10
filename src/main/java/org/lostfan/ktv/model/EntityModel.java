package org.lostfan.ktv.model;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.utils.Observable;

import java.util.List;
import java.util.Map;

public interface EntityModel<T extends Entity> extends Observable {

    List<T> getList();

    List<T> getListByForeignKey(int foreignKey);

    T getEntity(int id);

    String getEntityNameKey();

    String getEntityName();

    List<EntityField> getFields();

    List<FieldSearchCriterion<T>> getSearchCriteria();

    void setSearchCriteria(List<FieldSearchCriterion<T>> criteria);

    void saveOrEditEntity(Map<String, Object> fieldValues);

    void deleteEntityByRow(List<Integer> rowNumbers);

    List<EntityModel> getEntityModels();

    Class getEntityClass();

    List<EntityModel> getTableModels();

}
