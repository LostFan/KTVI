package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;

public interface SubscriberDAO {

    public List<Subscriber> getAllSubscribers();

    public Subscriber getSubscriber(int id);

    public void save(Subscriber subscriber);

    public void update(Subscriber subscriber);

    public void delete(int subscriberId);

    public int getBalanceByDay(int subscriberId, LocalDate date);

    public int getTariffByDay(int subscriberId, LocalDate date);

    public List<SubscriberSession> getSubscriberSessions(int subscriberId);

    public List<SubscriberTariff> getSubscriberTariffs(int subscriberId);

    public SubscriberSession getSubscriberSession(int subscriberSessionId);

    public void saveSubscriberSession(SubscriberSession subscriberSession);

    public void updateSubscriberSession(SubscriberSession subscriberSession);

    public SubscriberTariff getSubscriberTariff(int subscriberTariffId);

    public void saveSubscriberTariff(SubscriberTariff subscriberTariff);

    public void updateSubscriberTariff(SubscriberTariff subscriberTariff);
}
