package org.lostfan.ktv.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.validation.PaymentValidator;
import org.lostfan.ktv.validation.Validator;

public class PaymentEntityModel extends BaseEntityModel<Payment> {

    private LocalDate date;
    private List<EntityField> fields;
    private FullEntityField loadFullEntityField;
    private Validator<Payment> validator = new PaymentValidator();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

    public PaymentEntityModel() {

        date = LocalDate.now().withDayOfMonth(1);

        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("payment.id", EntityFieldTypes.Integer, Payment::getId, Payment::setId, false));
        this.fields.add(new EntityField("payment.payDate", EntityFieldTypes.Date, Payment::getDate, Payment::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, Payment::getSubscriberAccount, Payment::setSubscriberAccount));
        this.fields.add(new EntityField("service", EntityFieldTypes.Service, Payment::getServicePaymentId, Payment::setServicePaymentId));
        this.fields.add(new EntityField("renderedService", EntityFieldTypes.RenderedService, Payment::getRenderedServicePaymentId, Payment::setRenderedServicePaymentId));
        this.fields.add(new EntityField("payment.price", EntityFieldTypes.Integer, Payment::getPrice, Payment::setPrice));
        this.fields.add(new EntityField("payment.bankFileName", EntityFieldTypes.String, Payment::getBankFileName, Payment::setBankFileName));

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
    protected PaymentDAO getDao() {
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
        HashMap<Integer, Integer> hashMap = subscriberDAO.getServicesBalanceBySubscriberId(subscriberId);
        payment.setSubscriberAccount(subscriberId);
        payment.setDate(date);
        payment.setPaymentTypeId(null);
        payment.setPrice(price);
        payment.setServicePaymentId(FixedServices.SUBSCRIPTION_FEE.getId());
        return payment;
    }

    public List<Payment> createPayments(Integer subscriberId, LocalDate date, Integer price, String bankFileName, List<Payment> paymentsInList) {
        List<Payment> payments = new ArrayList<>();
        if (subscriberDAO.get(subscriberId) == null) {
            return payments;
        }
        HashMap<Integer, Integer> hashMap = subscriberDAO.getServicesBalanceBySubscriberId(subscriberId);
        for (Integer serviceId : hashMap.keySet()) {
            if(price == 0) {
                break;
            }
            if(serviceId == FixedServices.SUBSCRIPTION_FEE.getId()) {
                continue;
            }
            Integer renderedServicePrice = hashMap.get(serviceId);
            for (Payment payment : paymentsInList) {
                if(payment.getServicePaymentId().equals(serviceId)
                        && payment.getSubscriberAccount().equals(subscriberId)) {
                    renderedServicePrice-= payment.getPrice();
                }
            }
            if(renderedServicePrice <= 0) {
                continue;
            }
            Payment payment = new Payment();
            payment.setSubscriberAccount(subscriberId);
            payment.setDate(date);
            payment.setPaymentTypeId(null);
            payment.setBankFileName(bankFileName);
            if(price >= renderedServicePrice) {
                payment.setPrice(renderedServicePrice);
                price -= renderedServicePrice;
            } else {
                payment.setPrice(price);
                price = 0;
            }
            payment.setServicePaymentId(serviceId);
            payments.add(payment);
        }
        if(price == 0) {
            return payments;
        }
        Payment payment = new Payment();
        payment.setSubscriberAccount(subscriberId);
        payment.setDate(date);
        payment.setBankFileName(bankFileName);
        payment.setPaymentTypeId(null);
        payment.setPrice(price);
        payment.setServicePaymentId(FixedServices.SUBSCRIPTION_FEE.getId());
        payments.add(payment);
        return payments;
    }

    @Override
    public Validator<Payment> getValidator() {
        return this.validator;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        updateEntitiesList();
    }

    public List<Payment> getAll() {
        return DAOFactory.getDefaultDAOFactory().getPaymentDAO().getByMonth(this.date);
    }

    public LocalDate getDate() {
        return this.date;
    }

    public List<Payment> getPaymentsByBankFileName(String bankFileName) {
        return getDao().getPaymentsByBankFileName(bankFileName);
    }
}
