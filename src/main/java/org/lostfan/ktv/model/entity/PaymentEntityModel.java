package org.lostfan.ktv.model.entity;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.utils.PaymentsLoader;
import org.lostfan.ktv.validation.PaymentValidator;
import org.lostfan.ktv.validation.Validator;

public class PaymentEntityModel extends BaseEntityModel<Payment> {

    private LocalDate date;
    private List<EntityField> fields;
    private FullEntityField loadFullEntityField;
    private Validator<Payment> validator = new PaymentValidator();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private RenderedServiceDAO renderedServiceDAO = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();

    public PaymentEntityModel() {

        date = LocalDate.now().withDayOfMonth(1);


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

    public List<Payment> createPayments(File file, List<Payment> paymentsInList) {
        List<Payment> loadPayments= new PaymentsLoader(file).loadPayments();
        List<Payment> payments = new ArrayList<>();
        for (Payment loadPayment : loadPayments) {
            if(loadPayment.getPrice() == 0 || subscriberDAO.get(loadPayment.getSubscriberAccount()) == null) {
                continue;
            }

            payments.addAll(addPayments(loadPayment, paymentsInList));

        }
        return payments;
    }

    public List<Payment> addPayments(Payment loadPayment, List<Payment> paymentsInList) {
        List<Payment> payments = new ArrayList<>();
        Integer price = loadPayment.getPrice();
        Map<Integer, Integer> paymentMapFromTable = new HashMap<>();
        for (Payment payment : paymentsInList) {
            if(paymentMapFromTable.containsKey(payment.getRenderedServicePaymentId())) {
                paymentMapFromTable.put(payment.getRenderedServicePaymentId(),
                        paymentMapFromTable.get(payment.getRenderedServicePaymentId()) + payment.getPrice());
            } else {
                paymentMapFromTable.put(payment.getRenderedServicePaymentId(), payment.getPrice());
            }
        }

        HashMap<Integer, Integer> hashMap = subscriberDAO.getServicesBalanceBySubscriberId(loadPayment.getSubscriberAccount());
        for (Integer serviceId : hashMap.keySet()) {
            if(price == 0) {
                break;
            }
            if(hashMap.get(serviceId) == 0) {
                continue;
            }
            if(serviceId == FixedServices.SUBSCRIPTION_FEE.getId()) {
                continue;
            }
//            Integer renderedServicePrice = hashMap.get(serviceId);
//            for (Payment payment : paymentsInList) {
//                if(payment.getServicePaymentId().equals(serviceId)
//                        && payment.getSubscriberAccount().equals(loadPayment.getSubscriberAccount())) {
//                    renderedServicePrice-= payment.getPrice();
//                }
//            }
//            if(renderedServicePrice <= 0) {
//                continue;
//            }

            Map<Integer,Payment> paymentsForNotClosedRenderedServices =
                    getDao().getPaymentsForNotClosedRenderedServicesBySubscriberIdAndServiceId(loadPayment.getSubscriberAccount(), serviceId);
            for (Integer id : paymentsForNotClosedRenderedServices.keySet()) {
                Integer paymentPrice;
                if(paymentMapFromTable.get(id) == null) {
                    paymentPrice = paymentsForNotClosedRenderedServices.get(id).getPrice();
                } else {
                    if(paymentMapFromTable.get(id).equals(paymentsForNotClosedRenderedServices.get(id).getPrice())) {
                        continue;
                    }
                    paymentPrice = paymentsForNotClosedRenderedServices.get(id).getPrice() - paymentMapFromTable.get(id);
                }
                Payment payment = new Payment();
                payment.setSubscriberAccount(loadPayment.getSubscriberAccount());
                payment.setDate(date);
                payment.setPaymentTypeId(null);
                payment.setBankFileName(loadPayment.getBankFileName());
                if(price > paymentPrice) {
                    payment.setPrice(paymentPrice);
                    price -= paymentPrice;
                } else {
                    payment.setPrice(price);
                    price = 0;
                    payment.setServicePaymentId(serviceId);
                    payment.setRenderedServicePaymentId(id);
                    payments.add(payment);
                    return payments;
                }
                payment.setServicePaymentId(serviceId);
                payment.setRenderedServicePaymentId(id);
                payments.add(payment);
            }
//            Payment payment = new Payment();
//            payment.setSubscriberAccount(loadPayment.getSubscriberAccount());
//            payment.setDate(date);
//            payment.setPaymentTypeId(null);
//            payment.setBankFileName(loadPayment.getBankFileName());
//            if(price >= renderedServicePrice) {
//                payment.setPrice(renderedServicePrice);
//                price -= renderedServicePrice;
//            } else {
//                payment.setPrice(price);
//                price = 0;
//            }
//            payment.setServicePaymentId(serviceId);
//            payments.add(payment);
        }
//        if(price == 0) {
//            return payments;
//        }
        Payment payment = new Payment();
        payment.setSubscriberAccount(loadPayment.getSubscriberAccount());
        payment.setDate(date);
        payment.setBankFileName(loadPayment.getBankFileName());
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
