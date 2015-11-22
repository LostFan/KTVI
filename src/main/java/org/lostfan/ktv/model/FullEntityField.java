package org.lostfan.ktv.model;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FullEntityField extends EntityField {

    private List<EntityField> entityFields;

    public <T, V> FullEntityField(String titleKey, EntityFieldTypes type, Function<T, V> getter, BiConsumer<T, V> setter, boolean editable) {
        super(titleKey, type, getter, setter, editable);
    }

    public <T, V> FullEntityField(String titleKey, EntityFieldTypes type, Function<T, V> getter, BiConsumer<T, V> setter) {
        super(titleKey, type, getter, setter);
    }

    public void setEntityFields(List<EntityField> entityFields) {
        this.entityFields = entityFields;
    }

    public List<EntityField> getEntityFields() {
        return entityFields;
    }
}
