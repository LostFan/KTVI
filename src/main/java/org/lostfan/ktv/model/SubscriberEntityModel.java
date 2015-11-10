package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;

public class SubscriberEntityModel extends BaseEntityModel<Subscriber> {

    private List<EntityField> fields;

    private SubscriberDAO dao;
    private List<Subscriber> subscribers;

    public SubscriberEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("subscriber.id", EntityFieldTypes.Integer, Subscriber::getId, Subscriber::setId));
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

    public List<Subscriber> getList() {
        if (this.subscribers == null) {
            this.subscribers = this.dao.getAll();
        }

        return this.subscribers;
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
    public void setSearchCriteria(List<FieldSearchCriterion<Subscriber>> criteria) {
        super.setSearchCriteria(criteria);

        this.subscribers = this.dao.getAll();
        Stream<Subscriber> stream = this.subscribers.stream();
        for (FieldSearchCriterion<Subscriber> fieldSearchCriterion : criteria) {
            stream = stream.filter(fieldSearchCriterion.buildPredicate());
        }

        this.subscribers = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.subscribers = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public void saveOrEditEntity(Map<String, Object> fieldValues) {
        Subscriber subscriber = new Subscriber();
        subscriber.setAccount((String) fieldValues.get("subscriber.account"));
        subscriber.setName((String) fieldValues.get("subscriber.name"));
        subscriber.setStreetId((Integer) fieldValues.get("subscriber.street_id"));
        subscriber.setBalance((Integer) fieldValues.get("subscriber.balance"));
        subscriber.setConnected((Boolean) fieldValues.get("subscriber.connected"));
        if(fieldValues.get("subscriber.id") != null) {
            Integer subscriberId = (Integer) fieldValues.get("subscriber.id");
            if (this.dao.get(subscriberId) != null) {
                subscriber.setId((Integer) fieldValues.get("subscriber.id"));
                this.dao.update(subscriber);
            } else {
                this.dao.save(subscriber);
            }
        } else {
            this.dao.save(subscriber);
        }
        this.subscribers = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }

    //    @Override
//    public Map<Integer, String> getListByBeginningPartOfName(String str, Class clazz) {
//        return null;
//    }
}
