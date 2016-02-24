package org.lostfan.ktv.domain;

import java.time.LocalDate;

public class ServicePrice extends DefaultEntity {

    private Integer id;

    private int serviceId;

    private int price;

    private LocalDate date;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String getName() {
        return id.toString();
    }
}
