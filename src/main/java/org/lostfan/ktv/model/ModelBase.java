package org.lostfan.ktv.model;

import java.util.List;
import javax.swing.*;
import javax.swing.table.TableModel;

public interface ModelBase<T> {

    List<T> getList();

    TableModel getTableModel();

    String getEntityName();

    ComboBoxModel<String> getFields();

//    List<T> getSearchList(List<String> fields, List<String> values);

}
