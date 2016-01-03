package org.lostfan.ktv.view.model;


import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.table.AbstractTableModel;

public class SubscriptionFeeTableModel extends AbstractTableModel {


    private SubscriptionFeeModel model;
    public SubscriptionFeeTableModel(SubscriptionFeeModel model) {
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

    public SubscriptionFeeModel getSubscriptionFeeModel() {
        return this.model;
    }
}
