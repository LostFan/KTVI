package org.lostfan.ktv.model.searcher;

import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class SubscriberSearcherModel extends EntitySearcherModel<Subscriber> {


    public SubscriberSearcherModel() {
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }

    @Override
    protected EntityDAO<Subscriber> getDao() {
        return DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }
}
