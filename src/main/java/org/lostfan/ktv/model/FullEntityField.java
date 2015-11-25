package org.lostfan.ktv.model;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.lostfan.ktv.domain.Entity;

public class FullEntityField extends EntityField {

    private List<EntityField> entityFields;
    private Supplier<Entity> constructor;

    public <T, V> FullEntityField(String titleKey, EntityFieldTypes type, Function<T, V> getter, BiConsumer<T, V> setter, boolean editable, Supplier constructor) {
        super(titleKey, type, getter, setter, editable);
        this.constructor = constructor;
    }

    public <T, V> FullEntityField(String titleKey, EntityFieldTypes type, Function<T, V> getter, BiConsumer<T, V> setter, Supplier constructor) {
        super(titleKey, type, getter, setter);
        this.constructor = constructor;
    }

    public void setEntityFields(List<EntityField> entityFields) {
        this.entityFields = entityFields;
    }

    public List<EntityField> getEntityFields() {
        return entityFields;
    }

    public Entity createEntity() {
        return constructor.get();
    }
}
