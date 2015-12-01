package org.lostfan.ktv.domain;

import java.time.LocalDate;

public class TariffPrice extends DefaultEntity {

    private Integer id;

    private Integer tariffId;

    private int price;

    private LocalDate date;

    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return id.toString();
    }
}
