package org.lostfan.ktv.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityField<E, T> {

    public enum Types {
        String(java.lang.String.class),
        Integer(java.lang.Integer.class),
        Boolean(java.lang.Boolean.class),
        Date(LocalDate.class);

        private Class clazz;

        Types(Class clazz ) {
            this.clazz = clazz;
        }

        public Class getClazz() {
            return this.clazz;
        }

    }

    private String titleKey;
    private Types type;
    private Function<E, T> getter;
    private BiConsumer<E, T> setter;


    public EntityField(String titleKey, Types type, Function<E, T> getter, BiConsumer<E, T> setter) {
        this.titleKey = titleKey;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public Types getType() {
        return type;
    }

    public T get(E entity) {
        return getter.apply(entity);
    }

    public void set(E entity, T value) {
        setter.accept(entity, value);
    }
}
