package org.lostfan.ktv.view.model;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.table.DefaultTableModel;

public class EntityInnerTableModel<T extends Entity> extends DefaultTableModel {

    private class DeletedObject {
        private List<T> list;
        private int [] indexes;

        public DeletedObject(List<T> list, int[] indexes) {
            this.list = list;
            this.indexes = indexes;
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }

        public int[] getIndexes() {
            return indexes;
        }

        public void setIndexes(int[] indexes) {
            this.indexes = indexes;
        }
    }

    private static final String NUMBER_COLUMN_NAME = "number";

    private EntityModel<T> model;
    private int size = 0;
    private List<T> list;
    private List<DeletedObject> deletedObjects;
    private List<EntityField> entityFieldList;
    private EntityField parentField;
    private Object foreignId;

    public EntityInnerTableModel(EntityModel<T> model, Object foreignId) {

        this.model = model;
        this.foreignId = foreignId;
        this.entityFieldList = this.model.getEditableFieldsWithoutParent();
        this.parentField = this.model.getParentField();
        this.entityFieldList.add(0, new EntityField(NUMBER_COLUMN_NAME, EntityFieldTypes.Integer, e -> null, (e1, e2) -> {}));
        list = new ArrayList<>();
        deletedObjects = new ArrayList<>();
        size = this.model.getListByForeignKey((Integer)foreignId).size();

        for (T entity : this.model.getList()) {
            list.add(entity);
        }
    }

    public void addRow() {
        this.size++;
        list.add( model.createNewEntity());
    }

    public void addRow(Integer index) {
        if(index == -1) {
            return;
        }
        this.size++;
        T newEntity = model.createNewEntity();

        for (EntityField entityField : this.entityFieldList) {
            entityField.set(newEntity, entityField.get(list.get(index)));
        }
        list.add(newEntity);
    }

    public void deleteRows(int[] indexes) {
        if(indexes.length == 0) {
            return;
        }
//
        List<T> deletedList = new ArrayList<>();
        for (int i = 0; i <  indexes.length; i++) {
            deletedList.add(list.get(indexes[i]));
        }

        for (int i = indexes.length - 1; i >= 0; --i) {
            list.remove(indexes[i]);
            this.size--;
        }
        deletedObjects.add(new DeletedObject(deletedList, indexes));
        this.fireTableDataChanged();
    }

    public void restoreRows() {
        if(deletedObjects.size() == 0) {
            return;
        }
        int lastIndex = deletedObjects.size() - 1;
        int [] deletedIndexes = deletedObjects.get(lastIndex).getIndexes();
        List<T> deletedList = deletedObjects.get(lastIndex).getList();
        for (int i = 0; i < deletedIndexes.length; i++) {
            this.list.add(deletedIndexes[i], deletedList.get(i));
            this.size++;
        }
        deletedObjects.remove(lastIndex);

    }

    @Override
    public int getRowCount() {
        return size;
    }

    @Override
    public int getColumnCount() {
        return this.entityFieldList.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
    return ResourceBundles.getEntityBundle().getString(
            this.entityFieldList.get(columnIndex).getTitleKey());
}

    @Override
    public Class<?> getColumnClass(int columnIndex) {
            return this.entityFieldList.get(columnIndex).getType().getClazz();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(this.entityFieldList.get(columnIndex).getTitleKey() == NUMBER_COLUMN_NAME) {
            return false;
        }
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EntityFieldTypes thisType = this.entityFieldList.get(columnIndex).getType();
        if(this.entityFieldList.get(columnIndex).getTitleKey() == NUMBER_COLUMN_NAME) {
            return rowIndex + 1;
        }
        if (thisType.isEntityClass() && this.model.getFields().get(columnIndex).get(list.get(rowIndex)) != null) {
           return thisType.getDAO().get((Integer) this.model.getFields().get(columnIndex).get(list.get(rowIndex)));
        }
        return this.entityFieldList.get(columnIndex).get(list.get(rowIndex));

    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if(this.entityFieldList.get(columnIndex).getType().isEntityClass()) {
            if(value != null) {
                this.entityFieldList.get(columnIndex).set(list.get(rowIndex), ((Entity) value).getId());
            } else {
                this.entityFieldList.get(columnIndex).set(list.get(rowIndex), null);
            }
        } else {
            this.entityFieldList.get(columnIndex).set(list.get(rowIndex), value);
        }

    }

    public List<T> getEntityList() {
        for (T t : list) {
            this.parentField.set(t, foreignId);
        }
        return list;
    }
}
