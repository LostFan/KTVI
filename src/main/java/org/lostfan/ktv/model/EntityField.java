package org.lostfan.ktv.model;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityField<E, T> {

    private String titleKey;
    private EntityFieldTypes type;
    private Function<E, T> getter;
    private BiConsumer<E, T> setter;

    public EntityField(String titleKey, EntityFieldTypes type, Function<E, T> getter, BiConsumer<E, T> setter) {
        this.titleKey = titleKey;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public EntityFieldTypes getType() {
        return type;
    }

    public Object get(E entity) {
        return getter.apply(entity);
    }

    public void set(E entity, T value) {
        setter.accept(entity, value);
    }
}
