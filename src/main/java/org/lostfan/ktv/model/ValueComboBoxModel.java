package org.lostfan.ktv.model;

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

    public ValueComboBoxModel(EntityComboBoxModel<T> model) {
        this.model = model;
        values = this.model.getList().stream().collect(Collectors.toMap(criterion ->
                (String) this.model.getFields().get(1).get(criterion) +"(" + this.model.getFields().get(0).get(criterion) +")"
                ,  criterion -> {
            IdAndValue idAndValue = new IdAndValue();
            idAndValue.setId((Integer) this.model.getFields().get(0).get(criterion));
            idAndValue.setValue(this.model.getFields().get(1).get(criterion));
            return idAndValue;
        }));
    }

    public ValueComboBoxModel(EntityComboBoxModel<T> model, String currentValue) {
        this.model = model;

        values = this.model.getList().stream().collect(Collectors.toMap(criterion ->
                (String) this.model.getFields().get(1).get(criterion) +"(" + this.model.getFields().get(0).get(criterion) +")"
                ,  criterion -> {
            IdAndValue idAndValue = new IdAndValue();
            idAndValue.setId((Integer) this.model.getFields().get(0).get(criterion));
            idAndValue.setValue(this.model.getFields().get(1).get(criterion));
            return idAndValue;
        }));
        this.currentValue = (Object) currentValue;
    }

    public Integer getSelectedId() {
        if (this.currentValue == null) {
            return null;
        }
        return this.values.get(this.currentValue).getId();
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

    @Override
    public void setSelectedItem(Object anItem) {
            currentValue = anItem;
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
