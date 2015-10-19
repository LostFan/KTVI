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
    private EntityField entityFieldId;
    private EntityField entityFieldName;


    public SubscriberComboBoxModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

        this.fields = new ArrayList<>();
        entityFieldId = new EntityField<>("subscriber.id", EntityField.Types.Integer, Subscriber::getId, Subscriber::setId);
        entityFieldName = new EntityField<>("subscriber.name", EntityField.Types.String, Subscriber::getName, Subscriber::setName);
    }


    @Override
    public void setListByBeginningPartOfName(String str) {
        this.subscribers = dao.getSubscribersByBeginningPartOfName(str);
        this.notifyObservers(null);
    }

    @Override
    public List<Subscriber> getList() {
        if (this.subscribers == null) {
            this.subscribers = this.dao.getAllSubscribers();
        }

        return this.subscribers;
    }

    @Override
    public EntityField<Subscriber, ?> getEntityFieldName() {
        return entityFieldName;
    }

    @Override
    public EntityField<Subscriber, ?> getEntityFieldId() {
        return entityFieldId;
    }

    public ComboBoxModel getTableModel() {
        return new ValueComboBoxModel<>(this);
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }
}
