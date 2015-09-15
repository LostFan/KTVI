package org.lostfan.ktv.dao;

import java.util.List;

import org.lostfan.ktv.domain.SubscriberTariff;

/**
 * Created by Ihar_Niakhlebau on 15-Sep-15.
 */
public interface SubscriberTariffDAO {

    public List<SubscriberTariff> getAllSubscriberTariffs();

    public void save(SubscriberTariff subscriberSession);

    public void update(SubscriberTariff subscriberSession);

    public void delete(SubscriberTariff subscriberSession);
}
