package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.PaymentType;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class PaymentTypeSearcherModel extends EntitySearcherModel<PaymentType> {

    private List<EntityField> fields;

    public PaymentTypeSearcherModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("paymentType.id", EntityFieldTypes.Integer, PaymentType::getId, PaymentType::setId, false));
        this.fields.add(new EntityField("paymentType.name", EntityFieldTypes.String, PaymentType::getName, PaymentType::setName));
    }

    @Override
    public Class getEntityClass() {
        return PaymentType.class;
    }

    @Override
    public String getEntityNameKey() {
        return "paymentTypes";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<PaymentType> getDao() {
        return DAOFactory.getDefaultDAOFactory().getPaymentTypeDAO();
    }
}
