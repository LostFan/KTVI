package org.lostfan.ktv.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class ValueComboBoxModel<T> extends DefaultComboBoxModel<String> {

    public class IdAndValue
    {
        private int id;
        private Object value;

        public int getId() {
            return id;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public void setId(int id) {
            this.id = id;
        }

    }

    private Object currentValue;
    private EntityComboBoxModel<T> model;
    private Map<String, IdAndValue> values;
    private Integer id;

    public ValueComboBoxModel(EntityComboBoxModel<T> model) {
        setModel(model);
    }

    public void setModel(EntityComboBoxModel<T> model) {
        this.model = model;
        values = this.model.getList().stream().collect(Collectors.toMap(criterion ->
                (String) this.model.getFields().get(1).get(criterion) + "(" + this.model.getFields().get(0).get(criterion) + ")"
                , criterion -> {
            IdAndValue idAndValue = new IdAndValue();
            idAndValue.setId((Integer) this.model.getFields().get(0).get(criterion));
            idAndValue.setValue(this.model.getFields().get(1).get(criterion));
            return idAndValue;
        }));
    }

    public void setNewModel(EntityComboBoxModel<T> model, String currentValue) {
        this.model = model;
        values = this.model.getList().stream().collect(Collectors.toMap(criterion ->
                (String) this.model.getFields().get(1).get(criterion) + "(" + this.model.getFields().get(0).get(criterion) + ")"
                , criterion -> {
            IdAndValue idAndValue = new IdAndValue();
            idAndValue.setId((Integer) this.model.getFields().get(0).get(criterion));
            idAndValue.setValue(this.model.getFields().get(1).get(criterion));
            return idAndValue;
        }));
        this.currentValue = (Object) currentValue;
        if ((id != null && this.values.get(((String) this.currentValue +"(" + id.toString() +")")) == null)
                && this.values.get(((String) this.currentValue)) == null) {
            id =null;
        }

    }

    public Integer getSelectedId() {
        return this.id;
    }

    public String getSelectedName() {
        if (this.currentValue == null) {
            return null;
        }
        if(this.values.get(this.currentValue) == null) {
            return (String) this.currentValue;
        }
        return (String) this.values.get(this.currentValue).getValue();
    }

    public Object getSelectedNameById(int id) {

        return values.values().stream().filter(set -> set.getId() == id).findFirst().get().getValue();
    }

    public void setId(int id){
        this.id = id;
    }


    @Override
    public void setSelectedItem(Object anItem) {
        this.currentValue = anItem;
        if(this.values.get(this.currentValue) != null) {
            id = this.values.get(this.currentValue).getId();
        } else if (id != null && this.values.get(((String) this.currentValue +"(" + id.toString() +")")) == null){
            id = null;
        }
    }

    @Override
    public Object getSelectedItem() {
        return this.currentValue;
    }

    @Override
    public int getSize() {
        return this.model.getList().size();
    }

    @Override
    public String getElementAt(int index) {
        Object id = this.model.getFields().get(0).get(this.model.getList().get(index));
        Object value = this.model.getFields().get(1).get(this.model.getList().get(index)) +"(" + this.model.getFields().get(0).get(this.model.getList().get(index)) +")";
        return (String) value;
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
