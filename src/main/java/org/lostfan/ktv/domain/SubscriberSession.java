package org.lostfan.ktv.domain;

import java.time.LocalDate;

public class SubscriberSession extends DefaultEntity {

    private Integer id;

    private int subscriberAccount;

    private LocalDate connectionDate;

    private LocalDate disconnectionDate;

    private Integer disconnectionReasonId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(LocalDate connectionDate) {
        this.connectionDate = connectionDate;
    }

    public LocalDate getDisconnectionDate() {
        return disconnectionDate;
    }

    public void setDisconnectionDate(LocalDate disconnectionDate) {
        this.disconnectionDate = disconnectionDate;
    }

    public Integer getDisconnectionReasonId() {
        return disconnectionReasonId;
    }

    public void setDisconnectionReasonId(Integer disconnectionReasonId) {
        this.disconnectionReasonId = disconnectionReasonId;
    }

    public int getSubscriberAccount() {
        return subscriberAccount;
    }

    public void setSubscriberAccount(int subscriberAccount) {
        this.subscriberAccount = subscriberAccount;
    }

    @Override
    public String getName() {
        return id.toString();
    }
}
