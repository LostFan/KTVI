package org.lostfan.ktv.domain;

import java.util.List;

public class Subscriber {

    private String id;

    private String name;

    private int balance;

    private List<SubscriberSession> subscriberSessions;

    private boolean connected;

    private List<SubscriberTariff> subscriberTariffs;

    private List<RenderedService> renderedServices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<SubscriberSession> getSubscriberSessions() {
        return subscriberSessions;
    }

    public void setSubscriberSessions(List<SubscriberSession> subscriberSessions) {
        this.subscriberSessions = subscriberSessions;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public List<SubscriberTariff> getSubscriberTariffs() {
        return subscriberTariffs;
    }

    public void setSubscriberTariffs(List<SubscriberTariff> subscriberTariffs) {
        this.subscriberTariffs = subscriberTariffs;
    }

    public List<RenderedService> getRenderedServices() {
        return renderedServices;
    }

    public void setRenderedServices(List<RenderedService> renderedServices) {
        this.renderedServices = renderedServices;
    }
}
