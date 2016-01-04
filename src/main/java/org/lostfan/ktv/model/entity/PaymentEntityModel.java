package org.lostfan.ktv.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.validation.PaymentValidator;
import org.lostfan.ktv.validation.Validator;

public class PaymentEntityModel extends BaseEntityModel<Payment> {

    private List<EntityField> fields;
    private FullEntityField loadFullEntityField;
    private Validator<Payment> validator = new PaymentValidator();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

    public PaymentEntityModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("payment.id", EntityFieldTypes.Integer, Payment::getId, Payment::setId, false));
        this.fields.add(new EntityField("payment.payDate", EntityFieldTypes.Date, Payment::getDate, Payment::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, Payment::getSubscriberAccount, Payment::setSubscriberAccount));
        this.fields.add(new EntityField("service", EntityFieldTypes.Service, Payment::getServicePaymentId, Payment::setServicePaymentId));
        this.fields.add(new EntityField("payment.price", EntityFieldTypes.Integer, Payment::getPrice, Payment::setPrice));

        loadFullEntityField = new FullEntityField("payment", EntityFieldTypes.Payment, null, null, Payment::new);
        loadFullEntityField.setEntityFields(getFields().stream().filter(e -> !e.getTitleKey().equals("payment.id")).collect(Collectors.toList()));
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(MainModel.getServiceEntityModel());
        entityModels.add(MainModel.getSubscriberEntityModel());
        return entityModels;
    }

    @Override
    public String getEntityName() {
        return "payment";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "payments";
    }

    @Override
    public Class getEntityClass() {
        return Payment.class;
    }

    @Override
    protected EntityDAO<Payment> getDao() {
        return DAOFactory.getDefaultDAOFactory().getPaymentDAO();
    }

    @Override
    public Payment createNewEntity() {
        return new Payment();
    }

    public FullEntityField getLoadFullEntityField() {
        return loadFullEntityField;
    }

    public Payment createPayment(Integer subscriberId, LocalDate date, Integer price) {
        Payment payment = new Payment();
        if (subscriberDAO.get(subscriberId) == null) {
            return null;
        }
        payment.setSubscriberAccount(subscriberId);
        payment.setDate(date);
        payment.setPaymentTypeId(null);
        payment.setPrice(price);
        payment.setServicePaymentId(FixedServices.SUBSCRIPTION_FEE.getId());
        return payment;
    }

    @Override
    public Validator<Payment> getValidator() {
        return this.validator;
    }
}
