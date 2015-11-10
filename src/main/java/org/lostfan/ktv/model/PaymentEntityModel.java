package org.lostfan.ktv.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.domain.Payment;

public class PaymentEntityModel extends BaseEntityModel<Payment> {

    private List<EntityField<Payment, ?>> fields;

    private List<Payment> payments;

    public PaymentEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getPaymentDAO();
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("payment.id", EntityFieldTypes.Integer, Payment::getId, Payment::setId));
        this.fields.add(new EntityField<>("payment.payDate", EntityFieldTypes.Date, Payment::getDate, Payment::setDate));
        this.fields.add(new EntityField<>("subscriber", EntityFieldTypes.Subscriber, Payment::getSubscriberId, Payment::setSubscriberId));
        this.fields.add(new EntityField<>("service", EntityFieldTypes.Service, Payment::getServicePaymentId, Payment::setServicePaymentId));
        this.fields.add(new EntityField<>("payment.price", EntityFieldTypes.Integer, Payment::getPrice, Payment::setPrice));
    }



    @Override
    public void saveOrEditEntity(Map<String, Object> collect) {
        Payment payment = new Payment();
        payment.setPrice((Integer) collect.get("payment.price"));
        payment.setDate((LocalDate) collect.get("payment.payDate"));
        payment.setSubscriberId((Integer) collect.get("subscriber"));
        payment.setServicePaymentId((Integer) collect.get("service"));
        if(collect.get("payment.id") != null) {
            Integer paymentId = (Integer) collect.get("payment.id");
            if (this.dao.get(paymentId) != null) {
                payment.setId((Integer) collect.get("payment.id"));
                this.dao.update(payment);
            } else {
                this.dao.save(payment);
            }
        } else {
            this.dao.save(payment);
        }
        this.payments = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.payments = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(new ServiceEntityModel());
        entityModels.add(new SubscriberEntityModel());
        return entityModels;
    }

    @Override
    public String getEntityName() {
        return "payment";
    }

    @Override
    public List<EntityField<Payment, ?>> getFields() {
        return this.fields;
    }

    public List<Payment> getList() {
        if (this.payments == null) {
            this.payments = this.dao.getAll();
        }

        return this.payments;
    }

    @Override
    public List<Payment> getListByForeignKey(int foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "payments";
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Payment>> criteria) {
        super.setSearchCriteria(criteria);

        this.payments = this.dao.getAll();
        Stream<Payment> stream = this.payments.stream();
        for (FieldSearchCriterion<Payment> paymentFieldSearchCriterion : criteria) {
            stream = stream.filter(paymentFieldSearchCriterion.buildPredicate());
        }

        this.payments = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return Payment.class;
    }
}
