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
        private List<List<Object>> list;
        private int [] indexes;

        public DeletedObject(List<List<Object>> list, int[] indexes) {
            this.list = list;
            this.indexes = indexes;
        }

        public List<List<Object>> getList() {
            return list;
        }

        public void setList(List<List<Object>> list) {
            this.list = list;
        }

        public int[] getIndexes() {
            return indexes;
        }

        public void setIndexes(int[] indexes) {
            this.indexes = indexes;
        }
    }

    private EntityModel<T> model;
    private int size = 0;
    private List<List<Object>> list;
    private List<DeletedObject> deletedObjects;




    public EntityInnerTableModel(EntityModel<T> model, Object foreignId) {

        this.model = model;
        list = new ArrayList<>();
        deletedObjects = new ArrayList<>();
        if(foreignId != null) {
            size = this.model.getListByForeignKey((Integer)foreignId).size();
        }

        for (int i = 0; i < size ; i++) {

            List innerList = new ArrayList<>();
            list.add(innerList);
            for (EntityField entityField : this.model.getFields()) {
                Object value = entityField.get(this.model.getList().get(i));
                EntityFieldTypes thisType = this.model.getFields().get(i).getType();
                if (thisType.getClazz() == Integer.class && value ==null) {
                    value = 0;
                }
                if (thisType.isEntityClass()) {
                    value = thisType.getDAO().get((Integer) value).getName();
                }
                innerList.add(value);
            }
        }
    }

    public void addRow() {
        this.size++;
        list.add(createEmptyList());
    }

    private List<Object> createEmptyList() {
        List list1 = new ArrayList<>();
        for (int i = 0; i <  this.model.getFields().size(); i++) {
            list1.add(null);
        }
        return list1;
    }

    public void addRow(Integer index) {
        if(index == -1) {
            return;
        }
        this.size++;
        list.add(new ArrayList<>(list.get(index)));
    }

    public void deleteRows(int[] indexes) {
        if(indexes.length == 0) {
            return;
        }

        List<List<Object>> deletedList = new ArrayList<>();
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
        List<List<Object>> deletedList = deletedObjects.get(lastIndex).getList();
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
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        return list.get(rowIndex).get(columnIndex);
    }
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        list.get(rowIndex).set(columnIndex, value);
    }
}
