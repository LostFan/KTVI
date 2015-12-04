package org.lostfan.ktv.domain;

import java.time.LocalDate;

public class SubscriberTariff extends DefaultEntity{

    private Integer subscriberAccount;

    private Integer tariffId;

    private LocalDate connectTariff;

    private LocalDate disconnectTariff;

    public Integer getSubscriberAccount() {
        return subscriberAccount;
    }

    public void setSubscriberAccount(Integer subscriberAccount) {
        this.subscriberAccount = subscriberAccount;
    }

    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
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

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getId() {
        return null;
    }
}
