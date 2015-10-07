package org.lostfan.ktv.model;

import java.util.List;
import java.util.Observable;
import javax.swing.table.TableModel;

public interface Model<T> {

    List<T> getList();

    TableModel getTableModel();

    String getEntityName();

    List<EntityField<T, ?>> getFields();

    FieldsComboBoxModel<T> getFieldComboBoxModel();

    List<FieldSearchCriterion<T>> getSearchCriteria();

    void setSearchCriteria(List<FieldSearchCriterion<T>> criteria);
}
