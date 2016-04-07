package org.lostfan.ktv.model;

import org.lostfan.ktv.domain.Entity;

import java.time.LocalDate;
import java.util.function.Predicate;

public class FieldSearchCriterion<E extends Entity> {

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
        if(this.entityField.getType() == EntityFieldTypes.Subscriber) {
            return buildSubscriberPredicate();
        }
        if(this.entityField.getType().isEntityClass()) {
            return buildEntityPredicate();
        }
        switch (this.entityField.getType()) {
            case String:
                return buildStringPredicate();
            case Integer:
                return buildIntegerPredicate();
            case Double:
                return buildDoublePredicate();
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
            return e -> ((String) this.entityField.get(e)).toLowerCase().contains(((String) value).toLowerCase());
        } else if (cr == SearchCriteria.String.NotContains) {
            return e -> !((String) this.entityField.get(e)).toLowerCase().contains(((String) value).toLowerCase());
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

    private Predicate<E> buildDoublePredicate() {
        SearchCriteria.Double cr = (SearchCriteria.Double)this.criterion;
        if (cr == SearchCriteria.Double.Equals) {
            return e -> this.entityField.get(e).equals(value);
        } else if (cr == SearchCriteria.Double.GreaterThan) {
            return e -> ((Double) this.entityField.get(e)) > (Double) value;
        } else if (cr == SearchCriteria.Double.LessThan) {
            return e -> ((Double) this.entityField.get(e)) < (Double) value;
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

    private Predicate<E> buildEntityPredicate() {
        SearchCriteria.Entity cr = (SearchCriteria.Entity) this.criterion;
        if (cr == SearchCriteria.Entity.Equals) {
            return e -> {
                Object object = this.entityField.getType().getDAO().get((Integer) this.entityField.get(e));
                return ((Entity) object).getName().equalsIgnoreCase((String) value);
            };
        } else if (cr == SearchCriteria.Entity.Contains) {
            return e -> {
                Object object = this.entityField.getType().getDAO().get((Integer) this.entityField.get(e));
                return ((Entity) object).getName().toLowerCase().contains(((String) value).toLowerCase());
            };
        } else if (cr == SearchCriteria.Entity.NotContains) {
            return e -> {
                Object object = this.entityField.getType().getDAO().get((Integer) this.entityField.get(e));
                return !((Entity) object).getName().toLowerCase().contains(((String) value).toLowerCase());
            };
        }

        return e -> true;
    }

    private Predicate<E> buildSubscriberPredicate() {
        SearchCriteria.Entity cr = (SearchCriteria.Entity) this.criterion;
        if (cr == SearchCriteria.Entity.Equals) {
            return e -> {
                Object object = this.entityField.getType().getDAO().get((Integer) this.entityField.get(e));
                try {
                    Integer intValue = Integer.parseInt((String)value);
                    return ((Entity) object).getId().equals(intValue);
                } catch (NumberFormatException ex) {
                    return ((Entity) object).getName().equalsIgnoreCase((String) value);
                }

            };
        } else if (cr == SearchCriteria.Entity.Contains) {
            return e -> {
                Object object = this.entityField.getType().getDAO().get((Integer) this.entityField.get(e));
                try {
                    Integer intValue = Integer.parseInt((String)value);
                    return ((Entity) object).getId().equals(intValue);
                } catch (NumberFormatException ex) {
                    return ((Entity) object).getName().toLowerCase().contains(((String) value).toLowerCase());
                }
            };
        } else if (cr == SearchCriteria.Entity.NotContains) {
            return e -> {
                Object object = this.entityField.getType().getDAO().get((Integer) this.entityField.get(e));
                return !((Entity) object).getName().toLowerCase().contains(((String) value).toLowerCase());
            };
        }

        return e -> true;
    }
}
