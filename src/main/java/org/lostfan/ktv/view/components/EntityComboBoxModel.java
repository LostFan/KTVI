package org.lostfan.ktv.view.components;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntitySearcherModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListDataListener;

public class EntityComboBoxModel implements ComboBoxModel<String> {

    private class FormattedEntity {
        private Entity entity;
        private String formattedName;

        private FormattedEntity(Entity entity, String formattedName) {
            this.entity = entity;
            this.formattedName = formattedName;
        }
    }

    private Object currentValue;
    private Object currentFormattedValue;
    private EntitySearcherModel model;
    /**
     * ID of the selected entity.
     */
    private Integer id;
    /**
     * Key - the formatted name of an entity
     * Value - an entity index in the model list
     */
    private Map<String, Integer> formattedNames;
    private List<FormattedEntity> entities;

    public EntityComboBoxModel(EntitySearcherModel model) {
        setModel(model);
    }

    public void setModel(EntitySearcherModel model) {
        this.model = model;
        this.formattedNames = new HashMap<>(this.model.getList().size());
        this.entities = new ArrayList<>(this.model.getList().size());
        List<? extends Entity> entities = this.model.getList();
        for (int i = 0; i < entities.size(); ++i) {
            String formattedName = formatName(entities.get(i).getName(), entities.get(i).getId());
            this.formattedNames.put(formattedName, i);
            this.entities.add(new FormattedEntity(entities.get(i), formattedName));
        }
    }

    public void setNewModel(EntitySearcherModel model, String currentValue) {
        setModel(model);
        this.currentValue = currentValue;
        if (id != null && !this.formattedNames.containsKey(formatName(currentValue, id))
                && !this.formattedNames.containsKey(this.currentValue)) {
            id = null;
            currentFormattedValue = null;
        }
    }

    public Integer getSelectedId() {
        return this.id;
    }

    /**
     * Returns non-formatted value of the "Name" field of the entity
     */
    public String getSelectedName() {
        if (this.currentValue == null) {
            return null;
        }

        Integer index = this.formattedNames.get(this.currentValue);
        if (index != null) {
            return this.entities.get(index).entity.getName();
        }
        return String.valueOf(this.currentValue);
    }

    /**
     * Returns the value of the "name" field according to the current selected id.
     */
    public Object getSelectedNameById() {
        if (id == null) {
            return null;
        }

        for (FormattedEntity entity : entities) {
            if (entity.entity.getId().equals(id)) {
                return entity.entity.getName();
            }
        }

        return null;
    }

    public void setSelectedId(int id){
        this.id = id;
    }


    @Override
    public void setSelectedItem(Object anItem) {
        this.currentValue = anItem;

        // anItem is a formatted name
        if(formattedNames.containsKey(anItem)) {
            this.currentFormattedValue = anItem;
        }
        // currentFormattedValue is not the same as anItem
        else if(!(this.formattedNames.containsKey(this.currentFormattedValue)
                && this.entities.get(this.formattedNames.get(currentFormattedValue)).entity.getName().equals(anItem))) {
            currentFormattedValue = null;
        }

        if(this.formattedNames.containsKey(this.currentValue)) {
            id = this.entities.get(this.formattedNames.get(this.currentValue)).entity.getId();
        } else if (id != null && !this.formattedNames.containsKey(this.currentFormattedValue)){
            this.currentFormattedValue = null;
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
        if(index >= entities.size()) {
            return null;
        }
        return entities.get(index).formattedName;
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

    private String formatName(String name, Integer id) {
        return String.format("%s (%d)", name, id);
    }
}
