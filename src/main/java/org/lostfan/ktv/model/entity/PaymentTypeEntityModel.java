package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.PaymentType;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.PaymentTypeSearcherModel;

public class PaymentTypeEntityModel extends BaseEntityModel<PaymentType> {

    private List<EntityField> fields;

    public PaymentTypeEntityModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("paymentType.id", EntityFieldTypes.Integer, PaymentType::getId, PaymentType::setId, false));
        this.fields.add(new EntityField("paymentType.name", EntityFieldTypes.String, PaymentType::getName, PaymentType::setName));
    }

    @Override
    protected EntityDAO<PaymentType> getDao() {
        return DAOFactory.getDefaultDAOFactory().getPaymentTypeDAO();
    }

    @Override
    public String getEntityNameKey() {
        return "paymentTypes";
    }

    @Override
    public String getEntityName() {
        return "paymentType";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public PaymentType createNewEntity() {
        return new PaymentType();
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return PaymentType.class;
    }

    @Override
    public EntitySearcherModel<PaymentType> createSearchModel() {
        return new PaymentTypeSearcherModel();
    }
}
