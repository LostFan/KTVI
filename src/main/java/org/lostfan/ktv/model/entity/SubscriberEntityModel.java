package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.RenderedServiceExt;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.SubscriberSearcherModel;
import org.lostfan.ktv.model.transform.PaymentTransformer;
import org.lostfan.ktv.model.transform.RenderedServiceTransformer;
import org.lostfan.ktv.validation.SubscriberValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

public class SubscriberEntityModel extends BaseEntityModel<Subscriber> {

    private List<EntityField> fields;

    private Validator<Subscriber> validator = new SubscriberValidator();

    private RenderedServiceDAO renderedServiceDAO = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();
    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();

    private PaymentTransformer paymentTransformer = new PaymentTransformer();
    private RenderedServiceTransformer renderedServiceTransformer = new RenderedServiceTransformer();

    public SubscriberEntityModel() {

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("subscriber.account", EntityFieldTypes.Integer, Subscriber::getAccount, Subscriber::setAccount));
        this.fields.add(new EntityField("subscriber.name", EntityFieldTypes.String, Subscriber::getName, Subscriber::setName));
        this.fields.add(new EntityField("subscriber.street_id", EntityFieldTypes.Street, Subscriber::getStreetId, Subscriber::setStreetId));
//        this.fields.add(new EntityField("subscriber.balance", EntityFieldTypes.Integer, Subscriber::getBalance, Subscriber::setBalance));
//        this.fields.add(new EntityField("subscriber.connected", EntityFieldTypes.Boolean, Subscriber::isConnected, Subscriber::setConnected));
    }

    @Override
    public ValidationResult save(Subscriber entity) {
        ValidationResult result = getValidator().validate(entity);
        if (result.hasErrors()) {
            return result;
        }
        getDao().save(entity);
        updateEntitiesList();
        return result;
    }

    @Override
    public ValidationResult update(Subscriber entity) {
        ValidationResult result = getValidator().validate(entity);
        if (result.hasErrors()) {
            return result;
        }
        getDao().update(entity);
        updateEntitiesList();
        return result;
    }

    @Override
    public String getEntityName() {
        return "subscriber";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "subscribers";
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }


    @Override
    public Subscriber createNewEntity() {
        return new Subscriber();
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }

    @Override
    public Validator<Subscriber> getValidator() {
        return this.validator;
    }

    @Override
    protected EntityDAO<Subscriber> getDao() {
        return DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    public List<RenderedServiceExt> getRenderedServicesExtBySubscriberId(Integer id) {
        List<RenderedServiceExt> renderedServicesExt = new ArrayList<>();
        for (RenderedService renderedService : renderedServiceDAO.getBySubscriber(id)) {
            RenderedServiceExt renderedServiceExt = renderedServiceTransformer.transformTo(renderedService);
            renderedServiceExt.setService(serviceDAO.get(renderedServiceExt.getServiceId()));
            renderedServicesExt.add(renderedServiceExt);
        }
        return renderedServicesExt;
    }

    public List<PaymentExt> getPaymentsExtBySubscriberId(Integer id) {
        List<PaymentExt> paymentsExt = new ArrayList<>();
        for (Payment payment : paymentDAO.getBySubscriber(id)) {
            PaymentExt paymentExt = paymentTransformer.transformTo(payment);
            paymentExt.setService(serviceDAO.get(paymentExt.getServicePaymentId()));
            paymentsExt.add(paymentExt);
        }
        return paymentsExt;
    }

    @Override
    public EntitySearcherModel<Subscriber> createSearchModel() {
        return new SubscriberSearcherModel();
    }
}
