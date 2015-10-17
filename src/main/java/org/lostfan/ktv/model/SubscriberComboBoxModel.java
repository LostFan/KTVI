package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class SubscriberComboBoxModel extends EntityComboBoxModel<Subscriber> {

    private SubscriberDAO dao;
    private List<Subscriber> subscribers;
    private List<EntityField<Subscriber, ?>> fields;


    public SubscriberComboBoxModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("subscriber.id", EntityField.Types.Integer, Subscriber::getId, Subscriber::setId));
        this.fields.add(new EntityField<>("subscriber.name", EntityField.Types.String, Subscriber::getName, Subscriber::setName));
    }


    @Override
    public List<Subscriber> getListByBeginningPartOfName(String str) {
        this.subscribers = dao.getSubscribersByBeginningPartOfName(str);
        this.notifyObservers(null);
        return this.subscribers;
    }

    @Override
    public List<Subscriber> getList() {
        if (this.subscribers == null) {
            this.subscribers = this.dao.getAllSubscribers();
        }

        return this.subscribers;
    }

    @Override
    public List<EntityField<Subscriber, ?>> getFields() {
        return this.fields;
    }

    public ComboBoxModel getTableModel() {
        return new ValueComboBoxModel<>(this);
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }

    @Override
    public String getNameById(int id) {
        return this.dao.getSubscriber(id).getName();
    }
}
