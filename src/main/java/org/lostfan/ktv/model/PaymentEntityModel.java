package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
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
        this.fields.add(new EntityField<>("payment.price", EntityField.Types.Integer, Payment::getPrice, Payment::setPrice));
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
        for (FieldSearchCriterion<Payment> serviceFieldSearchCriterion : criteria) {
            stream = stream.filter(serviceFieldSearchCriterion.buildPredicate());
        }

        this.payments = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }
}
