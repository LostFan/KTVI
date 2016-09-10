package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.utils.ResourceBundles;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TurnoverSheetTableDTO {

    private Integer subscriberAccount;

    private LocalDate date;

    private BigDecimal broughtForwardBalanceCredit;

    private BigDecimal broughtForwardBalanceDebit;

    private BigDecimal carriedForwardBalanceCredit;

    private BigDecimal carriedForwardBalanceDebit;

    private BigDecimal turnoverBalanceCredit;

    private BigDecimal turnoverBalanceDebit;

    private Integer serviceId;

    private Subscriber subscriber;

    private Street subscriberStreet;

    public TurnoverSheetTableDTO() {
        this.broughtForwardBalanceCredit = BigDecimal.ZERO;
        this.broughtForwardBalanceDebit = BigDecimal.ZERO;
        this.carriedForwardBalanceCredit = BigDecimal.ZERO;
        this.carriedForwardBalanceDebit = BigDecimal.ZERO;
        this.turnoverBalanceCredit = BigDecimal.ZERO;
        this.turnoverBalanceDebit = BigDecimal.ZERO;
    }

    public Integer getSubscriberAccount() {
        return subscriberAccount;
    }

    public void setSubscriberAccount(Integer subscriberAccount) {
        this.subscriberAccount = subscriberAccount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getBroughtForwardBalanceCredit() {
        return broughtForwardBalanceCredit;
    }

    public void setBroughtForwardBalanceCredit(BigDecimal broughtForwardBalanceCredit) {
        this.broughtForwardBalanceCredit = broughtForwardBalanceCredit;
    }

    public BigDecimal getBroughtForwardBalanceDebit() {
        return broughtForwardBalanceDebit;
    }

    public void setBroughtForwardBalanceDebit(BigDecimal broughtForwardBalanceDebit) {
        this.broughtForwardBalanceDebit = broughtForwardBalanceDebit;
    }

    public BigDecimal getCarriedForwardBalanceCredit() {
        return carriedForwardBalanceCredit;
    }

    public void setCarriedForwardBalanceCredit(BigDecimal carriedForwardBalanceCredit) {
        this.carriedForwardBalanceCredit = carriedForwardBalanceCredit;
    }

    public BigDecimal getCarriedForwardBalanceDebit() {
        return carriedForwardBalanceDebit;
    }

    public void setCarriedForwardBalanceDebit(BigDecimal carriedForwardBalanceDebit) {
        this.carriedForwardBalanceDebit = carriedForwardBalanceDebit;
    }

    public BigDecimal getTurnoverBalanceCredit() {
        return turnoverBalanceCredit;
    }

    public void setTurnoverBalanceCredit(BigDecimal turnoverBalanceCredit) {
        this.turnoverBalanceCredit = turnoverBalanceCredit;
    }

    public BigDecimal getTurnoverBalanceDebit() {
        return turnoverBalanceDebit;
    }

    public void setTurnoverBalanceDebit(BigDecimal turnoverBalanceDebit) {
        this.turnoverBalanceDebit = turnoverBalanceDebit;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
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
