package org.lostfan.ktv.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.table.TableModel;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.domain.Payment;

public class PaymentEntityModel extends BaseEntityModel<Payment> {

    private List<EntityField<Payment, ?>> fields;

    private PaymentDAO dao;
    private List<Payment> payments;

    public PaymentEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getPaymentDAO();
        fields = new ArrayList<>();


        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("payment.id", EntityField.Types.Integer, Payment::getId, Payment::setId));
        this.fields.add(new EntityField<>("payment.payDate", EntityField.Types.Date, Payment::getDate, Payment::setDate));
        this.fields.add(new EntityField<>("subscriber", EntityField.Types.Subscriber, Payment::getSubscriberId, Payment::setSubscriberId));
        this.fields.add(new EntityField<>("service", EntityField.Types.Service, Payment::getServicePaymentId, Payment::setServicePaymentId));
        this.fields.add(new EntityField<>("payment.price", EntityField.Types.Integer, Payment::getPrice, Payment::setPrice));
    }

    @Override
    public void saveOrEditEntity(List<FieldValue<Payment>> fieldValues) {
        Map<String, FieldValue> collect =  fieldValues.stream().collect(Collectors.toMap(
                criterion -> criterion.getFieldName(),
                Function.identity()));
        Payment payment = new Payment();
        payment.setPrice((Integer) collect.get("payment.price").getValue());
        payment.setDate((LocalDate) collect.get("payment.price").getValue());
        if(collect.get("payment.id").getValue() != null) {
            Integer paymentId = (Integer) collect.get("payment.id").getValue();
            System.out.println(paymentId);

            if (this.dao.getPayment(paymentId) != null) {
                payment.setId((Integer) collect.get("payment.id").getValue());
                this.dao.update(payment);
            } else {
                this.dao.save(payment);
            }
        } else {
            this.dao.save(payment);
        }
        this.payments = this.dao.getAllPayments();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.payments = this.dao.getAllPayments();
        this.notifyObservers(null);
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
            this.payments = this.dao.getAllPayments();
        }

        return this.payments;
    }

    public TableModel getTableModel() {
        return new EntityTableModel<>(this);
    }

    @Override
    public String getEntityNameKey() {
        return "payments";
    }

    @Override
    public FieldsComboBoxModel<Payment> getFieldComboBoxModel() {
        return new FieldsComboBoxModel<>(fields);
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Payment>> criteria) {
        super.setSearchCriteria(criteria);

        this.payments = this.dao.getAllPayments();
        Stream<Payment> stream = this.payments.stream();
        for (FieldSearchCriterion<Payment> paymentFieldSearchCriterion : criteria) {
            stream = stream.filter(paymentFieldSearchCriterion.buildPredicate());
        }

        this.payments = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }
}
