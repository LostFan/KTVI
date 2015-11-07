package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.*;

public class HsqldbDaoFactory extends DAOFactory {

    private MaterialDAO materialDAO;
    private PaymentDAO paymentDAO;
    private RenderedServiceDAO renderedServiceDAO;
    private ServiceDAO serviceDAO;
    private SubscriberDAO subscriberDAO;
    private TariffDAO tariffDAO;
    private StreetDAO streetDAO;

    public HsqldbDaoFactory() {
        this.materialDAO = new HsqldbMaterialDAO();
        this.paymentDAO = new HsqldbPaymentDAO();
        this.renderedServiceDAO = new HsqldbRenderedServiceDAO();
        this.serviceDAO = new HsqldbServiceDAO();
        this.subscriberDAO = new HsqldbSubscriberDAO();
        this.tariffDAO = new HsqldbTariffDAO();
        this.streetDAO = new HsqldbStreetDAO();
    }

    @Override
    public MaterialDAO getMaterialDAO() {
        return this.materialDAO;
    }

    @Override
    public PaymentDAO getPaymentDAO() {
        return this.paymentDAO;
    }

    @Override
    public RenderedServiceDAO getRenderedServiceDAO() {
        return this.renderedServiceDAO;
    }

    @Override
    public ServiceDAO getServiceDAO() {
        return this.serviceDAO;
    }

    @Override
    public SubscriberDAO getSubscriberDAO() {
        return this.subscriberDAO;
    }

    @Override
    public TariffDAO getTariffDAO() {
        return this.tariffDAO;
    }

    @Override
    public StreetDAO getStreetDAO() {
        return this.streetDAO;
    }
}
