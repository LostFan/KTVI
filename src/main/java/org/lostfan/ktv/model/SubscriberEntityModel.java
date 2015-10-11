package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.table.TableModel;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;

public class SubscriberEntityModel extends BaseEntityModel<Subscriber> {

    private List<EntityField<Subscriber, ?>> fields;

    private SubscriberDAO dao;
    private List<Subscriber> subscribers;

    public SubscriberEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("subscriber.id", EntityField.Types.Integer, Subscriber::getId, Subscriber::setId));
        this.fields.add(new EntityField<>("subscriber.account", EntityField.Types.String, Subscriber::getAccount, Subscriber::setAccount));
        this.fields.add(new EntityField<>("subscriber.name", EntityField.Types.String, Subscriber::getName, Subscriber::setName));
        this.fields.add(new EntityField<>("subscriber.balance", EntityField.Types.Integer, Subscriber::getBalance, Subscriber::setBalance));
        this.fields.add(new EntityField<>("subscriber.connected", EntityField.Types.Boolean, Subscriber::isConnected, Subscriber::setConnected));
    }

    @Override
    public String getEntityName() {
        return "subscriber";
    }

    @Override
    public List<EntityField<Subscriber, ?>> getFields() {
        return this.fields;
    }

    public List<Subscriber> getList() {
        if (this.subscribers == null) {
            this.subscribers = this.dao.getAllSubscribers();
        }

        return this.subscribers;
    }

    public TableModel getTableModel() {
        return new EntityTableModel<>(this);
    }

    @Override
    public String getEntityNameKey() {
        return "subscribers";
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Subscriber>> criteria) {
        super.setSearchCriteria(criteria);

        this.subscribers = this.dao.getAllSubscribers();
        Stream<Subscriber> stream = this.subscribers.stream();
        for (FieldSearchCriterion<Subscriber> fieldSearchCriterion : criteria) {
            stream = stream.filter(fieldSearchCriterion.buildPredicate());
        }

        this.subscribers = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }
    @Override
    public void saveOrEditEntity(List<FieldValue<Subscriber>> fieldValues) {
        Map<String, FieldValue> collect =  fieldValues.stream().collect(Collectors.toMap(
                criterion -> criterion.getFieldName(),
                Function.identity()));
        Subscriber subscriber = new Subscriber();
        subscriber.setAccount((String) collect.get("subscriber.account").getValue());
        subscriber.setName((String) collect.get("subscriber.name").getValue());
        if(collect.get("subscriber.id") != null) {
            Integer subscriberId = (Integer) collect.get("subscriber.id").getValue();
            System.out.println(subscriberId);

            if (this.dao.getSubscriber(subscriberId) != null) {
                subscriber.setId((Integer) collect.get("subscriber.id").getValue());
                this.dao.update(subscriber);
            } else {
                this.dao.save(subscriber);
            }
        }
        this.subscribers = this.dao.getAllSubscribers();
        this.notifyObservers(null);
    }
}
