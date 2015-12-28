package org.lostfan.ktv.view.model;


import javax.swing.table.AbstractTableModel;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.utils.ResourceBundles;

public class EntitySearchTableModel<T extends Entity> extends AbstractTableModel {
    private EntitySearcherModel<T> model;
    public EntitySearchTableModel(EntitySearcherModel<T> model) {
        this.model = model;
        this.model.addObserver(args -> fireTableDataChanged());
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
        return this.model.getFields().get(columnIndex).getType().getValueClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.model.getFields().get(columnIndex).getType().isEntityClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = this.model.getFields().get(columnIndex).get(this.model.getList().get(rowIndex));
        EntityFieldTypes thisType = this.model.getFields().get(columnIndex).getType();
        if(thisType.getValueClass() == Integer.class && value ==null) {
            return 0;
        }
        if( thisType.isEntityClass()) {
            if(value != null) {
                value = thisType.getDAO().get((Integer) value);
            }
        }

        return value;
    }

    public EntitySearcherModel getEntitySearcherModel() {
        return this.model;
    }
}
