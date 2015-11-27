package org.lostfan.ktv.domain;

import java.time.LocalDate;

public class SubscriberTariff extends DefaultEntity{

    private Integer id;

    private Integer subscriberId;

    private Integer tariffId;

    private LocalDate connectTariff;

    private LocalDate disconnectTariff;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Integer subscriberId) {
        this.subscriberId = subscriberId;
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
        return id.toString();
    }
}
