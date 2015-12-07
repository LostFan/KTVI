package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;

public interface SubscriberDAO extends EntityDAO<Subscriber> {

    int getBalanceByDate(int subscriberId, LocalDate date);

    Integer getTariffIdByDate(int subscriberId, LocalDate date);

    Integer getSessionIdByDate(int subscriberId, LocalDate date);

    List<SubscriberSession> getSubscriberSessions(int subscriberId);

    List<SubscriberTariff> getSubscriberTariffs(int subscriberId);

    SubscriberSession getSubscriberSession(Integer subscriberId, LocalDate localDate);

    void saveSubscriberSession(SubscriberSession subscriberSession);

    SubscriberSession getNotClosedSubscriberSessionByDate(Integer subscriberId, LocalDate localDate);

    void updateSubscriberSession(SubscriberSession subscriberSession);

    void deleteSubscriberSession(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariff(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getNotClosedSubscriberTariffByDate(Integer subscriberId, LocalDate localDate);

    void saveSubscriberTariff(SubscriberTariff subscriberTariff);

    void updateSubscriberTariff(SubscriberTariff subscriberTariff);

    void deleteSubscriberTariff(Integer subscriberId, LocalDate localDate);

    List<Subscriber> getSubscribersByBeginningPartOfName(String str);

    List<Subscriber> getSubscribersByBeginningPartOfAccount(String str);

}
