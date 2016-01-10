package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.*;

public class PostGreDaoFactory extends DAOFactory {

    private DisconnectionReasonDAO disconnectionReasonDAO;
    private MaterialDAO materialDAO;
    private PaymentDAO paymentDAO;
    private RenderedServiceDAO renderedServiceDAO;
    private ServiceDAO serviceDAO;
    private SubscriberDAO subscriberDAO;
    private TariffDAO tariffDAO;
    private StreetDAO streetDAO;
    private MaterialConsumptionDAO materialConsumptionDAO;

    public PostGreDaoFactory() {
        this.disconnectionReasonDAO = new PostGreDisconnectionReasonDAO();
        this.materialDAO = new PostGreMaterialDAO();
        this.paymentDAO = new PostGrePaymentDAO();
        this.renderedServiceDAO = new PostGreRenderedServiceDAO();
        this.serviceDAO = new PostGreServiceDAO();
        this.subscriberDAO = new PostGreSubscriberDAO();
        this.tariffDAO = new PostGreTariffDAO();
        this.streetDAO = new PostGreStreetDAO();
        this.materialConsumptionDAO = new PostGreMaterialConsumptionDAO();
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

    @Override
    public MaterialConsumptionDAO getMaterialConsumptionDAO() {
        return this.materialConsumptionDAO ;
    }
}
