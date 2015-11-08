package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCriteria {

    public static class String extends SearchCriteria {

        static {
            criteria.put(EntityFieldTypes.String, new ArrayList<>());
        }

        private static void init() { }

        public static final String Equals = new String(EntityFieldTypes.String, "criteria.string.equals");
        public static final String Contains = new String(EntityFieldTypes.String, "criteria.string.contains");
        public static final String NotContains = new String(EntityFieldTypes.String, "criteria.string.notcontains");

        private String(EntityFieldTypes type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Integer extends SearchCriteria {

        static {
            criteria.put(EntityFieldTypes.Integer, new ArrayList<>());
        }

        private static void init() { }

        public static final Integer Equals = new Integer(EntityFieldTypes.Integer, "criteria.integer.equals");
        public static final Integer GreaterThan = new Integer(EntityFieldTypes.Integer, "criteria.integer.greater");
        public static final Integer LessThan = new Integer(EntityFieldTypes.Integer, "criteria.integer.less");

        private Integer(EntityFieldTypes type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Double extends SearchCriteria {

        static {
            criteria.put(EntityFieldTypes.Integer, new ArrayList<>());
        }

        private static void init() { }

        public static final Double Equals = new Double(EntityFieldTypes.Double, "criteria.integer.equals");
        public static final Double GreaterThan = new Double(EntityFieldTypes.Double, "criteria.integer.greater");
        public static final Double LessThan = new Double(EntityFieldTypes.Double, "criteria.integer.less");

        private Double(EntityFieldTypes type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Boolean extends SearchCriteria {

        static {
            criteria.put(EntityFieldTypes.Boolean, new ArrayList<>());
        }

        private static void init() { }

        public static final Boolean True = new Boolean(EntityFieldTypes.Boolean, "criteria.boolean.true");
        public static final Boolean False = new Boolean(EntityFieldTypes.Boolean, "criteria.boolean.false");

        private Boolean(EntityFieldTypes type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Date extends SearchCriteria {

        static {
            criteria.put(EntityFieldTypes.Date, new ArrayList<>());
        }

        private static void init() { }

        public static final Date Equals = new Date(EntityFieldTypes.Date, "criteria.date.equals");
        public static final Date EarlierThan = new Date(EntityFieldTypes.Date, "criteria.date.earlier");
        public static final Date LaterThan = new Date(EntityFieldTypes.Date, "criteria.date.later");

        private Date(EntityFieldTypes type, java.lang.String title) {
            super(type, title);
        }
    }

//    public static class Service extends SearchCriteria {
//
//        static {
//            criteria.put(EntityFieldTypes.Service, new ArrayList<>());
//        }
//
//        private static void init() { }
//
//        public static final Service Equals = new Service(EntityFieldTypes.Service, "criteria.string.equals");
//        public static final Service Contains = new Service(EntityFieldTypes.Service, "criteria.string.contains");
//        public static final Service NotContains = new Service(EntityFieldTypes.Service, "criteria.string.notcontains");
//
//        private Service(EntityFieldTypes type, java.lang.String title) {
//            super(type, title);
//        }
//    }
//
//    public static class Subscriber extends SearchCriteria {
//
//        static {
//            criteria.put(EntityFieldTypes.Subscriber, new ArrayList<>());
//        }
//
//        private static void init() { }
//
//        public static final Subscriber Equals = new Subscriber(EntityFieldTypes.Subscriber, "criteria.string.equals");
//        public static final Subscriber Contains = new Subscriber(EntityFieldTypes.Subscriber, "criteria.string.contains");
//        public static final Subscriber NotContains = new Subscriber(EntityFieldTypes.Subscriber, "criteria.string.notcontains");
//
//        private Subscriber(EntityFieldTypes type, java.lang.String title) {
//            super(type, title);
//        }
//    }

    public static class Entity extends SearchCriteria {

        static {
            criteria.put(EntityFieldTypes.Service, new ArrayList<>());
        }

        private static void init() { }

        public static final Entity Equals = new Entity(EntityFieldTypes.Service, "criteria.string.equals");
        public static final Entity Contains = new Entity(EntityFieldTypes.Service, "criteria.string.contains");
        public static final Entity NotContains = new Entity(EntityFieldTypes.Service, "criteria.string.notcontains");

        private Entity(EntityFieldTypes type, java.lang.String title) {
            super(type, title);
        }
    }

    private static Map<EntityFieldTypes, List<SearchCriteria>> criteria = new HashMap<>();
    private static List<SearchCriteria> criteriaSet = new ArrayList<>();

    static {
        // Initialize static content
        String.init();
        Integer.init();
        Boolean.init();
        Date.init();
//        Service.init();
//        Subscriber.init();
        Entity.init();
    }

    public static List<SearchCriteria> getCritera(EntityFieldTypes type) {
        if(type.isEntityClass())
            return criteriaSet;
        return criteria.get(type);
    }

    private final EntityFieldTypes type;
    private final java.lang.String title;

    private SearchCriteria(EntityFieldTypes type, java.lang.String title) {
        this.type = type;
        this.title = title;
        if(type.isEntityClass()) {
            criteriaSet.add(this);
        } else {
            criteria.get(type).add(this);
        }
    }

    public EntityFieldTypes getType() {
        return type;
    }

    public java.lang.String getTitle() {
        return title;
    }
}
