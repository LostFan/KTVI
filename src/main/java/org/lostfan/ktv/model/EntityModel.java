package org.lostfan.ktv.model;

import java.util.List;
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
}
