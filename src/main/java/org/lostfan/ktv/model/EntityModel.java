package org.lostfan.ktv.model;

import java.util.List;
import java.util.Map;
import javax.swing.table.TableModel;

public interface EntityModel<T> {

    List<T> getList();

    TableModel getTableModel();

    String getEntityNameKey();

    String getEntityName();

    List<EntityField<T, ?>> getFields();

    FieldsComboBoxModel<T> getFieldComboBoxModel();

    List<FieldSearchCriterion<T>> getSearchCriteria();

    void setSearchCriteria(List<FieldSearchCriterion<T>> criteria);

    void saveOrEditEntity(Map<String, Object> fieldValues);

    void deleteEntityByRow(List<Integer> rowNumbers);

    List<EntityComboBoxModel> getEntityComboBoxModels();

}
