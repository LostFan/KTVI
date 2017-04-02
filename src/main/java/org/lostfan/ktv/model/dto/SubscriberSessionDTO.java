package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.SubscriberSession;

import java.time.LocalDate;

public class SubscriberSessionDTO {

    public SubscriberSessionDTO() {

    }

    public SubscriberSessionDTO(SubscriberSession subscriberSession) {
        this.subscriberAccount = subscriberSession.getSubscriberAccount();
        this.connectionDate = subscriberSession.getConnectionDate();
        this.disconnectionDate = subscriberSession.getDisconnectionDate();
        this.disconnectionReasonId = subscriberSession.getDisconnectionReasonId();
    }

    private int subscriberAccount;

    private LocalDate connectionDate;

    private LocalDate disconnectionDate;

    private Integer disconnectionReasonId;

    private String disconnectionReason;

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

    public String getDisconnectionReason() {
        return disconnectionReason;
    }

    public void setDisconnectionReason(String disconnectionReason) {
        this.disconnectionReason = disconnectionReason;
    }
}
