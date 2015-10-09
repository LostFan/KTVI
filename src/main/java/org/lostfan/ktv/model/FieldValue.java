package org.lostfan.ktv.model;

import java.time.LocalDate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Ihar_Niakhlebau on 09-Oct-15.
 */
public class FieldValue<E> {
    private String fieldName;
    private Object value;

    public FieldValue(String fieldName,  Object value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue() {
        return value;
    }

}
