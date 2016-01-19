package org.lostfan.ktv.view.components;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;

import java.util.*;
import javax.swing.*;

public class EntityComboBoxModel extends DefaultComboBoxModel<String> {

    public interface EntitySelectedListener {

        void entitySelected(Entity entity);
    }

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
     * Selected entity.
     */
    private Entity entity;
    /**
     * Key - the formatted name of an entity
     * Value - an entity index in the model list
     */
    private Map<String, Integer> formattedNames;
    private List<FormattedEntity> entities;

    private Set<EntitySelectedListener> entitySelectedListeners;

    public EntityComboBoxModel(EntitySearcherModel model) {
        this.model = model;
        init();
        model.addObserver(args -> {
            init();
            fireContentsChanged(this, 0, getSize() - 1);
        });

        this.entitySelectedListeners = new HashSet<>();
    }

    private void init() {
        this.formattedNames = new HashMap<>(this.model.getList().size());
        this.entities = new ArrayList<>(this.model.getList().size());
        List<? extends Entity> entities = this.model.getList();
        for (int i = 0; i < entities.size(); ++i) {
            String formattedName = formatName(entities.get(i).getName(), entities.get(i).getId());
            this.formattedNames.put(formattedName, i);
            this.entities.add(new FormattedEntity(entities.get(i), formattedName));
        }
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
        if (this.entity != null && !this.formattedNames.containsKey(formatName(currentValue, this.entity.getId()))
                && !this.formattedNames.containsKey(this.currentValue)) {
            this.entity = null;
            currentFormattedValue = null;
        }
    }

    public Entity getSelectedEntity() {
        return this.entity;
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

    public void setSelectedEntity(int id){
        setSelectedEntity(this.model.getEntity(id));
    }

    public void setSelectedEntity(Entity entity) {
        this.entity = entity;
        if (this.entitySelectedListeners != null && this.entitySelectedListeners.size() > 0) {
            this.entitySelectedListeners.forEach(l -> l.entitySelected(entity));
        }
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.currentValue = anItem;
        if(this.entity!= null && this.currentValue.equals(this.entity.getName())) {
            return;
        }
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
            setSelectedEntity(this.entities.get(this.formattedNames.get(this.currentValue)).entity);
        } else if (this.entity != null && !this.formattedNames.containsKey(this.currentFormattedValue)){
            this.currentFormattedValue = null;
            this.entity = null;
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

    public void addEntitySelectedListener(EntitySelectedListener listener) {
        this.entitySelectedListeners.add(listener);
    }

    public void removeEntitySelectedListener(EntitySelectedListener listener) {
        this.entitySelectedListeners.remove(listener);
    }

    private String formatName(String name, Integer id) {
        return String.format("%s (%d)", name, id);
    }
}
