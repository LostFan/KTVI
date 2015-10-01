package org.lostfan.ktv.model;

import java.util.List;
import javax.swing.table.TableModel;

public interface TableModelBase<T> {

    List<T> getList();

    public TableModel getTableModel();

    String getTableName();
}
