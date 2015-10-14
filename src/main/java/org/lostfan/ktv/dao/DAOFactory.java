package org.lostfan.ktv.dao;

public abstract class DAOFactory {

    private static DAOFactory defaultFactory;

    public static void setDefaultDAOFactory(DAOFactory factory) {
        defaultFactory = factory;
    }

    public static DAOFactory getDefaultDAOFactory() {
        return defaultFactory;
    }

    public abstract MaterialDAO getMaterialDAO();

    public abstract PaymentDAO getPaymentDAO();

    public abstract RenderedServiceDAO getRenderedServiceDAO();

    public abstract ServiceDAO getServiceDAO();

    public abstract SubscriberDAO getSubscriberDAO();

    public abstract TariffDAO getTariffDAO();
}
