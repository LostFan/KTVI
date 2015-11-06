package org.lostfan.ktv.model;

import java.util.List;
import java.util.Map;

public interface EntityModel<T> {

    List<T> getList();

    String getEntityNameKey();

    String getEntityName();

    List<EntityField<T, ?>> getFields();

    List<FieldSearchCriterion<T>> getSearchCriteria();

    void setSearchCriteria(List<FieldSearchCriterion<T>> criteria);

    void saveOrEditEntity(Map<String, Object> fieldValues);

    void deleteEntityByRow(List<Integer> rowNumbers);

    List<EntityComboBoxModel> getEntityComboBoxModels();

    List<EntityModel> getEntityModels();

    Class getEntityClass();

}
