package org.lostfan.ktv.view.model;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.BaseEntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.utils.ResourceBundles;

public class EntitySelectionModel<T extends Entity> implements TableModel {

    private BaseEntityModel<T> model;
    private int size = 0;

    public EntitySelectionModel(BaseEntityModel<T> model) {

        this.model = model;
        size = this.model.getList().size();
    }

    @Override
    public int getRowCount() {
        return size;
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
        return this.model.getFields().get(columnIndex).getType().getClazz();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = this.model.getFields().get(columnIndex).get(this.model.getList().get(rowIndex));
        EntityFieldTypes thisType = this.model.getFields().get(columnIndex).getType();
        if (thisType.getClazz() == Integer.class && value ==null) {
            return 0;
        }
        if (thisType.isEntityClass()) {
            value = ((Entity) thisType.getDAO().get((Integer) value)).getName();
        }

        return value;
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
