package domain;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Ihar_Niakhlebau on 17-Aug-15.
 */
public class Subscriber {

    private String id;

    private String name;

    private int balance;

    private List<SubscriberSession> subscriberSessions;

    private boolean connected;

    private List<SubscriberTariff> subscriberTariffs;

    private List<SubscriberService> subscriberServices;

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

    public List<SubscriberService> getSubscriberServices() {
        return subscriberServices;
    }

    public void setSubscriberServices(List<SubscriberService> subscriberServices) {
        this.subscriberServices = subscriberServices;
    }
}
