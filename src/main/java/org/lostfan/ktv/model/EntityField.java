package org.lostfan.ktv.model;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Subscriber;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityField<E, T> {

    public enum Types {
        String(java.lang.String.class, false),
        Integer(java.lang.Integer.class, false),
        Boolean(java.lang.Boolean.class, false),
        Date(LocalDate.class, false),
        Subscriber(org.lostfan.ktv.domain.Subscriber.class, true),
        Service(org.lostfan.ktv.domain.Service.class, true);

        private Class clazz;
        private boolean entityClass;

        Types(Class clazz, boolean entityClass ) {
            this.clazz = clazz;
            this.entityClass = entityClass;
        }

        public Class getClazz() {
            return this.clazz;
        }

        public boolean isEntityClass() {
            return this.entityClass;
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
