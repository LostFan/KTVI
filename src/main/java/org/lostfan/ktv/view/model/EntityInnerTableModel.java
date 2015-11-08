package org.lostfan.ktv.view.model;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.BaseEntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EntityInnerTableModel<T extends Entity> implements TableModel {

    private BaseEntityModel<T> model;
    private Object foreignId;
    private int size = 0;

    public EntityInnerTableModel(BaseEntityModel<T> model, Object foreignId) {

        this.model = model;
        if(foreignId != null) {
            size = this.model.getListByForeignKey((Integer)foreignId).size();
        }

        this.foreignId = foreignId;
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
//        System.out.println(this.model.getFields().get(columnIndex).getType().isEntityClass());
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = this.model.getFields().get(columnIndex).get(this.model.getList().get(rowIndex));
        EntityFieldTypes thisType = this.model.getFields().get(columnIndex).getType();
        if(thisType.getClazz() == Integer.class && value ==null) {
            return 0;
        }
        if( thisType.isEntityClass()) {
            value =  ((Entity) thisType.getDAO().get((Integer) value)).getName();
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

    public EntityModel getEntityModel() {
        return this.model;
    }

}
