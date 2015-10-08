package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.table.TableModel;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;

public class SubscriberModel extends BaseModel<Subscriber> {

    private List<EntityField<Subscriber, ?>> fields;

    private SubscriberDAO dao;
    private List<Subscriber> subscribers;

    public SubscriberModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("ID", EntityField.Types.Integer, Subscriber::getId, Subscriber::setId));
        this.fields.add(new EntityField<>("Account", EntityField.Types.Integer, Subscriber::getAccount, Subscriber::setAccount));
        this.fields.add(new EntityField<>("Name", EntityField.Types.String, Subscriber::getName, Subscriber::setName));
        this.fields.add(new EntityField<>("Balance", EntityField.Types.Integer, Subscriber::getBalance, Subscriber::setBalance));
        this.fields.add(new EntityField<>("Connected", EntityField.Types.Boolean, Subscriber::isConnected, Subscriber::setConnected));
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
    public String getEntityName() {
        return "Абоненты";
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

}
