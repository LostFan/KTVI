package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCriteria {

    public static class String extends SearchCriteria {

        static {
            criteria.put(EntityField.Types.String, new ArrayList<>());
        }

        private static void init() { }

        public static final String Equals = new String(EntityField.Types.String, "Равно");
        public static final String Contains = new String(EntityField.Types.String, "Содержит");
        public static final String NotContains = new String(EntityField.Types.String, "Не содержит");

        private String(EntityField.Types type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Integer extends SearchCriteria {

        static {
            criteria.put(EntityField.Types.Integer, new ArrayList<>());
        }

        private static void init() { }

        public static final Integer Equals = new Integer(EntityField.Types.Integer, "Равно");
        public static final Integer GreaterThan = new Integer(EntityField.Types.Integer, "Больше чем");
        public static final Integer LessThan = new Integer(EntityField.Types.Integer, "Меньше чем");

        private Integer(EntityField.Types type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Boolean extends SearchCriteria {

        static {
            criteria.put(EntityField.Types.Boolean, new ArrayList<>());
        }

        private static void init() { }

        public static final Boolean True = new Boolean(EntityField.Types.Boolean, "Да");
        public static final Boolean False = new Boolean(EntityField.Types.Boolean, "Нет");

        private Boolean(EntityField.Types type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Date extends SearchCriteria {

        static {
            criteria.put(EntityField.Types.Date, new ArrayList<>());
        }

        private static void init() { }

        public static final Date Equals = new Date(EntityField.Types.Date, "Равно");
        public static final Date EarlierThan = new Date(EntityField.Types.Date, "Раньше чем");
        public static final Date LaterThan = new Date(EntityField.Types.Date, "Позже чем");

        private Date(EntityField.Types type, java.lang.String title) {
            super(type, title);
        }
    }

    private static Map<EntityField.Types, List<SearchCriteria>> criteria = new HashMap<>();

    static {
        // Initialize static content
        String.init();
        Integer.init();
        Boolean.init();
        Date.init();
    }

    public static List<SearchCriteria> getCritera(EntityField.Types type) {
        return criteria.get(type);
    }

    private final EntityField.Types type;
    private final java.lang.String title;

    private SearchCriteria(EntityField.Types type, java.lang.String title) {
        this.type = type;
        this.title = title;
        criteria.get(type).add(this);
    }

    public EntityField.Types getType() {
        return type;
    }

    public java.lang.String getTitle() {
        return title;
    }
}