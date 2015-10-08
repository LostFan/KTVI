package org.lostfan.ktv.model;

import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EntityTableModel<T> implements TableModel {

    private BaseModel<T> model;

    public EntityTableModel(BaseModel<T> model) {
        this.model = model;
    }

    @Override
    public int getRowCount() {
        return this.model.getList().size();
    }

    @Override
    public int getColumnCount() {
        return this.model.getFields().size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return ResourceBundles.getEntityBundle().getString(
                this.model.getFields().get(columnIndex).getTitleKey());
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.model.getFields().get(columnIndex).get(this.model.getList().get(rowIndex));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
