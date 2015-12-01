package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.*;

public class HsqldbDaoFactory extends DAOFactory {

    private DisconnectionReasonDAO disconnectionReasonDAO;
    private MaterialDAO materialDAO;
    private PaymentDAO paymentDAO;
    private RenderedServiceDAO renderedServiceDAO;
    private ServiceDAO serviceDAO;
    private ServicePriceDAO servicePriceDAO;
    private SubscriberDAO subscriberDAO;
    private TariffDAO tariffDAO;
    private TariffPriceDAO tariffPriceDAO;
    private StreetDAO streetDAO;
    private MaterialConsumptionDAO materialConsumptionDAO;

    public HsqldbDaoFactory() {
        this.disconnectionReasonDAO = new HsqldbDisconnectionReasonDAO();
        this.materialDAO = new HsqldbMaterialDAO();
        this.paymentDAO = new HsqldbPaymentDAO();
        this.renderedServiceDAO = new HsqldbRenderedServiceDAO();
        this.serviceDAO = new HsqldbServiceDAO();
        this.servicePriceDAO = new HsqldbServicePriceDAO();
        this.subscriberDAO = new HsqldbSubscriberDAO();
        this.tariffDAO = new HsqldbTariffDAO();
        this.tariffPriceDAO = new HsqldbTariffPriceDAO();
        this.streetDAO = new HsqldbStreetDAO();
        this.materialConsumptionDAO = new HsqldbMaterialConsumptionDAO();
    }

    @Override
    public DisconnectionReasonDAO getDisconnectionReasonDAO() {
        return this.disconnectionReasonDAO;
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
    public ServicePriceDAO getServicePriceDAO() {
        return this.servicePriceDAO;
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
    public TariffPriceDAO getTariffPriceDAO() {
        return this.tariffPriceDAO;
    }

    @Override
    public StreetDAO getStreetDAO() {
        return this.streetDAO;
    }

    @Override
    public MaterialConsumptionDAO getMaterialConsumptionDAO() {
        return this.materialConsumptionDAO ;
    }
}
