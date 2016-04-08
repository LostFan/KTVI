package org.lostfan.ktv.utils;

import org.lostfan.ktv.domain.Subscriber;

import java.util.Comparator;

public class SubscriberByAddressComparator implements Comparator<Subscriber> {

    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2) {
        if (subscriber1.getStreetId() - subscriber2.getStreetId() != 0) {
            return subscriber1.getStreetId() - subscriber2.getStreetId();
        }
        if (subscriber1.getHouse() - subscriber2.getHouse() != 0) {
            return subscriber1.getHouse() - subscriber2.getHouse();
        }
        if (subscriber1.getIndex().compareToIgnoreCase(subscriber2.getIndex()) != 0) {
            return subscriber1.getIndex().compareToIgnoreCase(subscriber2.getIndex());
        }
        if (subscriber1.getBuilding().compareToIgnoreCase(subscriber2.getBuilding()) != 0) {
            return subscriber1.getBuilding().compareToIgnoreCase(subscriber2.getBuilding());
        }
        if (subscriber1.getFlat().length() - subscriber2.getFlat().length() != 0) {
            return subscriber1.getFlat().length() - subscriber2.getFlat().length();
        }
        return subscriber1.getFlat().compareToIgnoreCase(subscriber2.getFlat());
    }
}
