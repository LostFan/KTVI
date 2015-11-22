package org.lostfan.ktv.model;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;

public enum EntityFieldTypes {

    String(java.lang.String.class, false, null),
    Integer(java.lang.Integer.class, false, null),
    Boolean(java.lang.Boolean.class, false, null),
    Date(LocalDate.class, false, null),
    Double(java.lang.Double.class, false, null),
    Subscriber(org.lostfan.ktv.domain.Subscriber.class, true, DAOFactory.getDefaultDAOFactory().getSubscriberDAO()),
    Service(org.lostfan.ktv.domain.Service.class, true, DAOFactory.getDefaultDAOFactory().getServiceDAO()),
    Street(org.lostfan.ktv.domain.Street.class, true, DAOFactory.getDefaultDAOFactory().getStreetDAO()),
    Material(org.lostfan.ktv.domain.Material.class, true, DAOFactory.getDefaultDAOFactory().getMaterialDAO()),
    Payment(org.lostfan.ktv.domain.Payment.class, true, DAOFactory.getDefaultDAOFactory().getPaymentDAO()),
    RenderedService(org.lostfan.ktv.domain.RenderedService.class, true, DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO()),
    List(java.util.List.class, true, null);

    private Class clazz;
    private boolean entityClass;
    private EntityDAO dao;

    EntityFieldTypes(Class clazz, boolean entityClass, EntityDAO dao) {
        this.clazz = clazz;
        this.entityClass = entityClass;
        this.dao = dao;
    }

    public static EntityFieldTypes getEntityClass(Class aClass) {
        for (EntityFieldTypes o : EntityFieldTypes.class.getEnumConstants()) {
            if(aClass == o.getClazz()) {
                return o;
            }
        };
        return null;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public boolean isEntityClass() {
        return this.entityClass;
    }

    public EntityDAO getDAO() {
        return this.dao;
    }

}
