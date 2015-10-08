package org.lostfan.ktv.model;

import java.time.LocalDate;
import java.util.function.Predicate;

public class FieldSearchCriterion<E> {

    private EntityField entityField;
    private SearchCriteria criterion;
    private Object value;

    public FieldSearchCriterion(EntityField entityField, SearchCriteria criterion, Object value) {
        this.entityField = entityField;
        this.criterion = criterion;
        this.value = value;
    }

    public EntityField getEntityField() {
        return entityField;
    }

    public SearchCriteria getCriterion() {
        return criterion;
    }

    public Object getValue() {
        return value;
    }

    public Predicate<E> buildPredicate() {
        switch (this.criterion.getType()) {
            case String:
                return buildStringPredicate();
            case Integer:
                return buildIntegerPredicate();
            case Boolean:
                return buildBooleanPredicate();
            case Date:
                return buildDatePredicate();
            default:
                return e -> true;
        }
    }

    private Predicate<E> buildStringPredicate() {
        SearchCriteria.String cr = (SearchCriteria.String)this.criterion;
        if (cr == SearchCriteria.String.Equals) {
            return e -> ((String) this.entityField.get(e)).equalsIgnoreCase((String) value);
        } else if (cr == SearchCriteria.String.Contains) {
            return e -> ((String) this.entityField.get(e)).contains((String) value);
        } else if (cr == SearchCriteria.String.NotContains) {
            return e -> !((String) this.entityField.get(e)).contains((String) value);
        }

        return e -> true;
    }

    private Predicate<E> buildIntegerPredicate() {
        SearchCriteria.Integer cr = (SearchCriteria.Integer)this.criterion;
        if (cr == SearchCriteria.Integer.Equals) {
            return e -> this.entityField.get(e).equals(value);
        } else if (cr == SearchCriteria.Integer.GreaterThan) {
            return e -> ((Integer) this.entityField.get(e)) > (Integer) value;
        } else if (cr == SearchCriteria.Integer.LessThan) {
            return e -> ((Integer) this.entityField.get(e)) < (Integer) value;
        }

        return e -> true;
    }

    private Predicate<E> buildBooleanPredicate() {
        return e -> this.value != null && value == this.entityField.get(e);
    }

    private Predicate<E> buildDatePredicate() {
        SearchCriteria.Date cr = (SearchCriteria.Date)this.criterion;
        if (cr == SearchCriteria.Date.Equals) {
            return e -> ((LocalDate) this.entityField.get(e)).isEqual((LocalDate) value);
        } else if (cr == SearchCriteria.Date.EarlierThan) {
            return e -> ((LocalDate) this.entityField.get(e)).isBefore((LocalDate) value);
        } else if (cr == SearchCriteria.Date.LaterThan) {
            return e -> ((LocalDate) this.entityField.get(e)).isAfter((LocalDate) value);
        }

        return e -> true;
    }
}
