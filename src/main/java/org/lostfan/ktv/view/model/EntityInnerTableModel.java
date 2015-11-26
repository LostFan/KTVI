package org.lostfan.ktv.view.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.table.DefaultTableModel;

public class EntityInnerTableModel extends DefaultTableModel {

    private class DeletedObject {
        private List<Entity> list;
        private int [] indexes;

        public DeletedObject(List<Entity> list, int[] indexes) {
            this.list = list;
            this.indexes = indexes;
        }

        public List<Entity> getList() {
            return list;
        }

        public void setList(List<Entity> list) {
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

//    private EntityModel<T> model;
    private int size = 0;
    private List<Entity> list;
    private List<DeletedObject> deletedObjects;
    private List<EntityField> entityFieldList;
    private FullEntityField fullEntityField;
    private Object foreignId;

    public EntityInnerTableModel(FullEntityField fullEntityField, List<Entity> list) {

//        this.model = model;
        this.foreignId = foreignId;
        this.fullEntityField = fullEntityField;
        this.entityFieldList = fullEntityField.getEntityFields().stream().collect(Collectors.toList());
//        this.parentField = this.model.getParentField();
        this.entityFieldList.add(0, new EntityField(NUMBER_COLUMN_NAME, EntityFieldTypes.Integer, e -> null, (e1, e2) -> {}));
        this.list = new ArrayList<>();
        deletedObjects = new ArrayList<>();
        size = list.size();

        for (Entity entity : list) {
            this.list.add(entity);
        }
    }

    public void addRow() {
        this.size++;
        list.add( this.fullEntityField.createEntity());
    }

    public void addRow(Integer index) {
        if(index == -1) {
            return;
        }
        this.size++;
        Entity newEntity = this.fullEntityField.createEntity();

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
        List<Entity> deletedList = new ArrayList<>();
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
        List<Entity> deletedList = deletedObjects.get(lastIndex).getList();
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
            return this.entityFieldList.get(columnIndex).getType().getValueClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.entityFieldList.get(columnIndex).getTitleKey() != NUMBER_COLUMN_NAME;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EntityFieldTypes thisType = this.entityFieldList.get(columnIndex).getType();
        if(this.entityFieldList.get(columnIndex).getTitleKey() == NUMBER_COLUMN_NAME) {
            return rowIndex + 1;
        }
        if (thisType.isEntityClass() && this.entityFieldList.get(columnIndex).get(list.get(rowIndex)) != null) {
           return thisType.getDAO().get((Integer) this.entityFieldList.get(columnIndex).get(list.get(rowIndex)));
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

    public List<Entity> getEntityList() {
        return list;
    }
}
