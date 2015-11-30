package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.MainModel;

public class PaymentEntityModel extends BaseEntityModel<Payment> {

    private List<EntityField> fields;

    public PaymentEntityModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("payment.id", EntityFieldTypes.Integer, Payment::getId, Payment::setId, false));
        this.fields.add(new EntityField("payment.payDate", EntityFieldTypes.Date, Payment::getDate, Payment::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, Payment::getSubscriberAccount, Payment::setSubscriberAccount));
        this.fields.add(new EntityField("service", EntityFieldTypes.Service, Payment::getServicePaymentId, Payment::setServicePaymentId));
        this.fields.add(new EntityField("payment.price", EntityFieldTypes.Integer, Payment::getPrice, Payment::setPrice));
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
    public List<Payment> getListByForeignKey(Integer foreignKey) {
        return null;
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
}
