package org.lostfan.ktv.domain;

import java.time.LocalDate;

public class SubscriberTariff {

    private int id;

    private int subscriberId;

    private int tariffId;

    private LocalDate connectTariff;

    private LocalDate disconnectTariff;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public int getTariffId() {
        return tariffId;
    }

    public void setTariffId(int tariffId) {
        this.tariffId = tariffId;
    }

    public LocalDate getConnectTariff() {
        return connectTariff;
    }

    public void setConnectTariff(LocalDate connectTariff) {
        this.connectTariff = connectTariff;
    }

    public LocalDate getDisconnectTariff() {
        return disconnectTariff;
    }

    public void setDisconnectTariff(LocalDate disconnectTariff) {
        this.disconnectTariff = disconnectTariff;
    }
}
