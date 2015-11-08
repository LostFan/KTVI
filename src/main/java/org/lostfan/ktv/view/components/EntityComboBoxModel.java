package org.lostfan.ktv.view.components;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntitySearcherModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class EntityComboBoxModel<T extends Entity> extends DefaultComboBoxModel<String> {

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
    private Object currentValueWithId;
    private EntitySearcherModel<T> model;
    private Map<String, IdAndValue> values;
    private Integer id;
    private List<String> nameWithIdList;

    public EntityComboBoxModel(EntitySearcherModel<T> model) {
        setModel(model);
    }

    public void setModel(EntitySearcherModel<T> model) {
        this.model = model;
        nameWithIdList = new ArrayList<>();
        values = this.model.getList().stream().collect(Collectors.toMap(entity -> {
                String formattedName = String.format("%s (%d)", entity.getName(), entity.getId());
                nameWithIdList.add(formattedName);
                return formattedName;
            } , entity -> {
                IdAndValue idAndValue = new IdAndValue();
                idAndValue.setId(entity.getId());
                idAndValue.setValue(entity.getName());
                return idAndValue;
        }));
    }

    public void setNewModel(EntitySearcherModel<T> model, String currentValue) {
        setModel(model);
        this.currentValue = (Object) currentValue;
        if (id != null && !this.values.containsKey(currentValue +"(" + id +")")
                && !this.values.containsKey(this.currentValue)) {
            id = null;
            currentValueWithId = null;
        }

    }

    public Integer getSelectedId() {
        return this.id;
    }

    public String getSelectedName() {
        if (this.currentValue == null) {
            return null;
        }
        if(!this.values.containsKey(this.currentValue)) {
            return (String) this.currentValue;
        }
        return (String) this.values.get(this.currentValue).getValue();
    }

    public Object getSelectedNameById() {
        return values.values().stream().filter(set -> set.getId() == id).findFirst().get().getValue();
    }

    public void setId(int id){
        this.id = id;
    }


    @Override
    public void setSelectedItem(Object anItem) {
        this.currentValue = anItem;
        if(values.containsKey(anItem)) {
            this.currentValueWithId = anItem;
        } else if(!(this.values.containsKey(this.currentValueWithId) && this.values.get(currentValueWithId).getValue().equals(anItem))) {
            currentValueWithId = null;
        }
        if(this.values.containsKey(this.currentValue)) {
            id = this.values.get(this.currentValue).getId();
        } else if (id != null && !this.values.containsKey(this.currentValueWithId)){
            this.currentValueWithId = null;
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
//        Object id = this.model.getEntityFieldId().get(this.model.getList().get(index));
//        Object value = this.model.getEntityFieldName().get(this.model.getList().get(index)) +"(" + this.model.getEntityFieldId().get(this.model.getList().get(index)) +")";
        if(index >= nameWithIdList.size()) {
            return null;
        }
        return nameWithIdList.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
