package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class SubscriberSearcherModel extends EntitySearcherModel<Subscriber> {

    private SubscriberDAO dao;
    private List<Subscriber> subscribers;

    public SubscriberSearcherModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public void setSearchQuery(String query) {
        this.subscribers = dao.getSubscribersByBeginningPartOfName(query);
        this.notifyObservers(null);
    }

    @Override
    public List<Subscriber> getList() {
        if (this.subscribers == null) {
            this.subscribers = this.dao.getAll();
        }

        return this.subscribers;
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }
}
