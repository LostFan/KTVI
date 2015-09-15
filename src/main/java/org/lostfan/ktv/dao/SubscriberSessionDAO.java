package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.SubscriberSession;

public interface SubscriberSessionDAO {

    public List<SubscriberSession> getAllSubscriberSessions();

    public void save(SubscriberSession subscriberSession);

    public void update(SubscriberSession subscriberSession);

    public void delete(SubscriberSession subscriberSession);
}
