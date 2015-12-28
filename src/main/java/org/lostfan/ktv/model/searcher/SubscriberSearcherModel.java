package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class SubscriberSearcherModel extends EntitySearcherModel<Subscriber> {

    private List<EntityField> fields;

    public SubscriberSearcherModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("subscriber.account", EntityFieldTypes.Integer, Subscriber::getAccount, Subscriber::setAccount));
        this.fields.add(new EntityField("subscriber.name", EntityFieldTypes.String, Subscriber::getName, Subscriber::setName));
        this.fields.add(new EntityField("subscriber.street_id", EntityFieldTypes.Street, Subscriber::getStreetId, Subscriber::setStreetId));
        this.fields.add(new EntityField("subscriber.balance", EntityFieldTypes.Integer, Subscriber::getBalance, Subscriber::setBalance));
        this.fields.add(new EntityField("subscriber.connected", EntityFieldTypes.Boolean, Subscriber::isConnected, Subscriber::setConnected));
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }

    @Override
    public String getEntityNameKey() {
        return "subscribers";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<Subscriber> getDao() {
        return DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }
}
