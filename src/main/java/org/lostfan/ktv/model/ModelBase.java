package org.lostfan.ktv.model;

import java.util.List;
import javax.swing.*;
import javax.swing.table.TableModel;

public interface ModelBase<T> {

    List<T> getList();

    public TableModel getTableModel();

    String getTableName();

    ComboBoxModel<String> getFields();

    String getName();

//    List<T> getSearchList(List<String> fields, List<String> values);

}
