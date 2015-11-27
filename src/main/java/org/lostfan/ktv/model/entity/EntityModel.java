package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.utils.Observable;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

import java.util.List;
import java.util.Map;

public interface EntityModel<T extends Entity> extends Observable {

    List<T> getList();

    List<T> getListByForeignKey(Integer foreignKey);

    T getEntity(int id);

    T getFullEntity(int id);

    String getEntityNameKey();

    String getEntityName();

    List<EntityField> getFields();

    List<FullEntityField> getFullFields();

    List<FieldSearchCriterion<T>> getSearchCriteria();

    void setSearchCriteria(List<FieldSearchCriterion<T>> criteria);

    ValidationResult save(T entity);

    T createNewEntity();

    void deleteEntityByRow(List<Integer> rowNumbers);

    void deleteEntityById(Integer id);

    List<EntityModel> getEntityModels();

    Class getEntityClass();

    Validator<T> getValidator();

    T buildDTO(T entity, Map<String, List<Entity>> map);

}
