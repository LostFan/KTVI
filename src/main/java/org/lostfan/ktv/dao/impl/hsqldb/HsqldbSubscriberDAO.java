package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;

import java.time.LocalDate;
import java.util.List;

public class HsqldbSubscriberDAO implements SubscriberDAO {

    public List<Subscriber> getAllSubscribers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Subscriber getSubscriber(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void save(Subscriber subscriber) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void update(Subscriber subscriber) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void delete(Subscriber subscriber) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getBalanceByDay(int subscriberId, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getTariffByDay(int subscriberId, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<SubscriberSession> getSubscriberSessions(int subscriberId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<SubscriberTariff> getSubscriberTariffs(int subscriberId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public SubscriberSession getSubscriberSession(int subscriberSessionId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void saveSubscriberSession(SubscriberSession subscriberSession) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateSubscriberSession(SubscriberSession subscriberSession) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public SubscriberTariff getSubscriberTariff(int subscriberTariffId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void saveSubscriberTariff(SubscriberTariff subscriberTariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateSubscriberTariff(SubscriberTariff subscriberTariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
