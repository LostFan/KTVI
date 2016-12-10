package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.domain.Subscriber;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriberDebit {

    private Integer subscriberAccount;

    private BigDecimal debit;

    private Subscriber subscriber;

    private Street subscriberStreet;

    public SubscriberDebit() {
        this.debit = BigDecimal.ZERO;
    }

    public Integer getSubscriberAccount() {
        return subscriberAccount;
    }

    public void setSubscriberAccount(Integer subscriberAccount) {
        this.subscriberAccount = subscriberAccount;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Street getSubscriberStreet() {
        return subscriberStreet;
    }

    public void setSubscriberStreet(Street subscriberStreet) {
        this.subscriberStreet = subscriberStreet;
    }
}
