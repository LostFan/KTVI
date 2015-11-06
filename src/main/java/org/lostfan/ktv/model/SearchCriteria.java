package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchCriteria {

    public static class String extends SearchCriteria {

        static {
            criteria.put(Types.String, new ArrayList<>());
        }

        private static void init() { }

        public static final String Equals = new String(Types.String, "criteria.string.equals");
        public static final String Contains = new String(Types.String, "criteria.string.contains");
        public static final String NotContains = new String(Types.String, "criteria.string.notcontains");

        private String(Types type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Integer extends SearchCriteria {

        static {
            criteria.put(Types.Integer, new ArrayList<>());
        }

        private static void init() { }

        public static final Integer Equals = new Integer(Types.Integer, "criteria.integer.equals");
        public static final Integer GreaterThan = new Integer(Types.Integer, "criteria.integer.greater");
        public static final Integer LessThan = new Integer(Types.Integer, "criteria.integer.less");

        private Integer(Types type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Double extends SearchCriteria {

        static {
            criteria.put(Types.Integer, new ArrayList<>());
        }

        private static void init() { }

        public static final Double Equals = new Double(Types.Double, "criteria.integer.equals");
        public static final Double GreaterThan = new Double(Types.Double, "criteria.integer.greater");
        public static final Double LessThan = new Double(Types.Double, "criteria.integer.less");

        private Double(Types type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Boolean extends SearchCriteria {

        static {
            criteria.put(Types.Boolean, new ArrayList<>());
        }

        private static void init() { }

        public static final Boolean True = new Boolean(Types.Boolean, "criteria.boolean.true");
        public static final Boolean False = new Boolean(Types.Boolean, "criteria.boolean.false");

        private Boolean(Types type, java.lang.String title) {
            super(type, title);
        }
    }

    public static class Date extends SearchCriteria {

        static {
            criteria.put(Types.Date, new ArrayList<>());
        }

        private static void init() { }

        public static final Date Equals = new Date(Types.Date, "criteria.date.equals");
        public static final Date EarlierThan = new Date(Types.Date, "criteria.date.earlier");
        public static final Date LaterThan = new Date(Types.Date, "criteria.date.later");

        private Date(Types type, java.lang.String title) {
            super(type, title);
        }
    }

//    public static class Service extends SearchCriteria {
//
//        static {
//            criteria.put(Types.Service, new ArrayList<>());
//        }
//
//        private static void init() { }
//
//        public static final Service Equals = new Service(Types.Service, "criteria.string.equals");
//        public static final Service Contains = new Service(Types.Service, "criteria.string.contains");
//        public static final Service NotContains = new Service(Types.Service, "criteria.string.notcontains");
//
//        private Service(Types type, java.lang.String title) {
//            super(type, title);
//        }
//    }
//
//    public static class Subscriber extends SearchCriteria {
//
//        static {
//            criteria.put(Types.Subscriber, new ArrayList<>());
//        }
//
//        private static void init() { }
//
//        public static final Subscriber Equals = new Subscriber(Types.Subscriber, "criteria.string.equals");
//        public static final Subscriber Contains = new Subscriber(Types.Subscriber, "criteria.string.contains");
//        public static final Subscriber NotContains = new Subscriber(Types.Subscriber, "criteria.string.notcontains");
//
//        private Subscriber(Types type, java.lang.String title) {
//            super(type, title);
//        }
//    }

    public static class Entity extends SearchCriteria {

        static {
            criteria.put(Types.Service, new ArrayList<>());
        }

        private static void init() { }

        public static final Entity Equals = new Entity(Types.Service, "criteria.string.equals");
        public static final Entity Contains = new Entity(Types.Service, "criteria.string.contains");
        public static final Entity NotContains = new Entity(Types.Service, "criteria.string.notcontains");

        private Entity(Types type, java.lang.String title) {
            super(type, title);
        }
    }

    private static Map<Types, List<SearchCriteria>> criteria = new HashMap<>();
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

    public static List<SearchCriteria> getCritera(Types type) {
        if(type.isEntityClass())
            return criteriaSet;
        return criteria.get(type);
    }

    private final Types type;
    private final java.lang.String title;

    private SearchCriteria(Types type, java.lang.String title) {
        this.type = type;
        this.title = title;
        if(type.isEntityClass()) {
            criteriaSet.add(this);
        } else {
            criteria.get(type).add(this);
        }
    }

    public Types getType() {
        return type;
    }

    public java.lang.String getTitle() {
        return title;
    }
}
