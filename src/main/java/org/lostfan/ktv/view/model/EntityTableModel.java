package org.lostfan.ktv.view.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.table.AbstractTableModel;

public class EntityTableModel<T extends Entity> extends AbstractTableModel {

    private EntityModel<T> model;
    public EntityTableModel(EntityModel<T> model) {
        this.model = model;
        this.model.addObserver(args -> fireTableDataChanged());
    }

    @Override
    public int getRowCount() {
        return this.model.getList().size();
    }

    @Override
    public int getColumnCount() {
        return getFields().size();
    }

    @Override
    public String getColumnName(int columnIndex) {
    return ResourceBundles.getEntityBundle().getString(
            getFields().get(columnIndex).getTitleKey());
}

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getFields().get(columnIndex).getType().getValueClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getFields().get(columnIndex).getType().isEntityClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = getFields().get(columnIndex).get(this.model.getList().get(rowIndex));
        EntityFieldTypes thisType = getFields().get(columnIndex).getType();
        if (thisType.getValueClass() == Integer.class && value == null) {
            return 0;
        }
        if (thisType.isEntityClass()) {
            if (value != null) {
                value = thisType.getDAO().get((Integer) value);
            }
        }
        if (thisType.getValueClass() == LocalDate.class && value != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return dateTimeFormatter.format((LocalDate) value);
        }

        return value;
    }

    public EntityModel getEntityModel() {
        return this.model;
    }

    private List<EntityField> getFields() {
        return model.getFields().stream().filter(e -> e.isTableVisible()).collect(Collectors.toList());
    }
}
