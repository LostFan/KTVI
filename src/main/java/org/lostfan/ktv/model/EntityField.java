package org.lostfan.ktv.model;

import org.lostfan.ktv.domain.Entity;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityField {

    private String titleKey;
    private EntityFieldTypes type;
    private Function getter;
    private BiConsumer setter;
    private boolean editable;
    private boolean tableVisible;

    public <T, V> EntityField(String titleKey, EntityFieldTypes type, Function<T, V> getter, BiConsumer<T, V> setter, boolean editable, boolean tableVisible) {
        this.titleKey = titleKey;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.editable = editable;
        this.tableVisible = tableVisible;
    }

    public <T, V> EntityField(String titleKey, EntityFieldTypes type, Function<T, V> getter, BiConsumer<T, V> setter, boolean editable) {
        this.titleKey = titleKey;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.editable = editable;
        this.tableVisible = true;
    }

    public <T, V> EntityField(String titleKey, EntityFieldTypes type, Function<T, V> getter, BiConsumer<T, V> setter) {
        this(titleKey, type, getter, setter, true);
    }

    public String getTitleKey() {
        return titleKey;
    }

    public EntityFieldTypes getType() {
        return type;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isTableVisible() {
        return tableVisible;
    }

    public void setTableVisible(boolean tableVisible) {
        this.tableVisible = tableVisible;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> Object get(T entity) {
        return getter.apply(entity);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void set(T entity, Object value) {
        setter.accept(entity, value);
    }
}
