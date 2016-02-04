package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.HashMap;
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

    SubscriberSession getSubscriberSessionByConnectionDate(Integer subscriberAccount, LocalDate connectionDate);

    SubscriberSession getSubscriberSessionByDisconnectionDate(Integer subscriberAccount, LocalDate disconnectionDate);

    void saveSubscriberSession(SubscriberSession subscriberSession);

    SubscriberSession getSubscriberSessionBySubscriberIdAndAfterDate(Integer subscriberId, LocalDate localDate);

    SubscriberSession getNotClosedSubscriberSession(Integer subscriberAccount, LocalDate connectionDate);

    SubscriberSession getSubscriberSessionBySubscriberIdAndContainDate(Integer subscriberId, LocalDate localDate);

    void updateSubscriberSession(SubscriberSession subscriberSession);

    void deleteSubscriberSession(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariffBySubscriberIdAndConnectionDate(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariffBySubscriberIdAndDisconnectionDate(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariffBySubscriberIdAndContainDate(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariffBySubscriberIdAndAfterDate(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getNotClosedSubscriberTariffByDate(Integer subscriberId, LocalDate localDate);

    List<SubscriberTariff> getSubscriberTariffsBySubscriberIdInMonth(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariffBySubscriberIdInAllMonth(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariffBySubscriberIdInMonthBeginInPrevMonthEndInCurrentMonth(Integer subscriberId, LocalDate localDate);

    List<SubscriberTariff> getSubscriberTariffsBySubscriberIdInMonthBeginInCurrentMonthEndInCurrentMonth(Integer subscriberId, LocalDate localDate);

    SubscriberTariff getSubscriberTariffBySubscriberIdInMonthBeginInCurrentMonth(Integer subscriberId, LocalDate localDate);

    void saveSubscriberTariff(SubscriberTariff subscriberTariff);

    void updateSubscriberTariff(SubscriberTariff subscriberTariff);

    void deleteSubscriberTariff(Integer subscriberId, LocalDate localDate);

    List<Subscriber> getSubscribersByBeginningPartOfName(String str);

    List<Subscriber> getSubscribersByBeginningPartOfAccount(String str);

    HashMap<Integer, Integer> getServicesBalanceBySubscriberIdAndDate(Integer subscriberId, LocalDate date);

    HashMap<Integer, Integer> getServicesBalance(Integer subscriberAccount);
}
