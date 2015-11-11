package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.validation.SubscriberValidator;
import org.lostfan.ktv.validation.Validator;

public class SubscriberEntityModel extends BaseEntityModel<Subscriber> {

    private List<EntityField> fields;

    private Validator<Subscriber> validator = new SubscriberValidator();

    public SubscriberEntityModel() {

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("subscriber.id", EntityFieldTypes.Integer, Subscriber::getId, Subscriber::setId, false));
        this.fields.add(new EntityField("subscriber.account", EntityFieldTypes.String, Subscriber::getAccount, Subscriber::setAccount));
        this.fields.add(new EntityField("subscriber.name", EntityFieldTypes.String, Subscriber::getName, Subscriber::setName));
        this.fields.add(new EntityField("subscriber.street_id", EntityFieldTypes.Street, Subscriber::getStreetId, Subscriber::setStreetId));
        this.fields.add(new EntityField("subscriber.balance", EntityFieldTypes.Integer, Subscriber::getBalance, Subscriber::setBalance));
        this.fields.add(new EntityField("subscriber.connected", EntityFieldTypes.Boolean, Subscriber::isConnected, Subscriber::setConnected));
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
    public List<Subscriber> getListByForeignKey(int foreignKey) {
        return null;
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
}
