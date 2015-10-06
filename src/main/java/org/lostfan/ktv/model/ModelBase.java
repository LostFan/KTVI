package org.lostfan.ktv.model;

import java.util.List;
import javax.swing.table.TableModel;

public interface ModelBase<T> {

    List<T> getList();

    public TableModel getTableModel();

    String getTableName();

    List<String> getFields();

    String getName();

}
