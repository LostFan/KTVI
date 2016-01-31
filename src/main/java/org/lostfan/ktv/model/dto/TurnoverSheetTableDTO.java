package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.utils.ResourceBundles;

import java.time.LocalDate;

public class TurnoverSheetTableDTO {

    private Integer subscriberAccount;

    private LocalDate date;

    private Integer broughtForwardBalanceCredit;

    private Integer broughtForwardBalanceDebit;

    private Integer carriedForwardBalanceCredit;

    private Integer carriedForwardBalanceDebit;

    private Integer turnoverBalanceCredit;

    private Integer turnoverBalanceDebit;

    private Integer serviceId;

    private Subscriber subscriber;

    private Street subscriberStreet;

    public TurnoverSheetTableDTO() {
        this.broughtForwardBalanceCredit = 0;
        this.broughtForwardBalanceDebit = 0;
        this.carriedForwardBalanceCredit = 0;
        this.carriedForwardBalanceDebit = 0;
        this.turnoverBalanceCredit = 0;
        this.turnoverBalanceDebit = 0;
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

    public Integer getBroughtForwardBalanceCredit() {
        return broughtForwardBalanceCredit;
    }

    public void setBroughtForwardBalanceCredit(Integer broughtForwardBalanceCredit) {
        this.broughtForwardBalanceCredit = broughtForwardBalanceCredit;
    }

    public Integer getBroughtForwardBalanceDebit() {
        return broughtForwardBalanceDebit;
    }

    public void setBroughtForwardBalanceDebit(Integer broughtForwardBalanceDebit) {
        this.broughtForwardBalanceDebit = broughtForwardBalanceDebit;
    }

    public Integer getCarriedForwardBalanceCredit() {
        return carriedForwardBalanceCredit;
    }

    public void setCarriedForwardBalanceCredit(Integer carriedForwardBalanceCredit) {
        this.carriedForwardBalanceCredit = carriedForwardBalanceCredit;
    }

    public Integer getCarriedForwardBalanceDebit() {
        return carriedForwardBalanceDebit;
    }

    public void setCarriedForwardBalanceDebit(Integer carriedForwardBalanceDebit) {
        this.carriedForwardBalanceDebit = carriedForwardBalanceDebit;
    }

    public Integer getTurnoverBalanceCredit() {
        return turnoverBalanceCredit;
    }

    public void setTurnoverBalanceCredit(Integer turnoverBalanceCredit) {
        this.turnoverBalanceCredit = turnoverBalanceCredit;
    }

    public Integer getTurnoverBalanceDebit() {
        return turnoverBalanceDebit;
    }

    public void setTurnoverBalanceDebit(Integer turnoverBalanceDebit) {
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

    public String getFullSubscriberAddress() {
        if (subscriber == null || subscriberStreet == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(subscriberStreet.getName())
                .append(",")
                .append(subscriber.getHouse())
                .append(subscriber.getIndex());
        if (!subscriber.getBuilding().isEmpty()) {
            stringBuffer
                    .append(",")
                    .append(ResourceBundles.getGuiBundle().getString("buildingAbbreviated"))
                    .append(subscriber.getBuilding());
        }
        stringBuffer
                .append(",")
                .append(ResourceBundles.getGuiBundle().getString("flatAbbreviated"))
                .append(subscriber.getFlat());
        return stringBuffer.toString();
    }

    public String getAbbreviatedName() {
            String abbreviatedName = subscriber.getName();
            String[] strings = abbreviatedName.split("\\s+");
            if (strings.length < 2) {
                return subscriber.getName();
            }
            if (strings.length == 2) {
                return new StringBuffer()
                        .append(strings[0])
                        .append(" ")
                        .append(strings[1].charAt(0))
                        .append(".")
                        .toString();
            }
            return new StringBuffer()
                    .append(strings[0])
                    .append(" ")
                    .append(strings[1].charAt(0))
                    .append(". ")
                    .append(strings[2].charAt(0))
                    .append(". ")
                    .toString();
    }
}

