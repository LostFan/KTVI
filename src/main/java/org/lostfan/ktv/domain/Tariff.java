package org.lostfan.ktv.domain;

import java.util.List;

public class Tariff {

    int id;

    private String name;

    private String channels;

    private List<TariffPrice> tariffPrices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public List<TariffPrice> getTariffPrices() {
        return tariffPrices;
    }

    public void setTariffPrices(List<TariffPrice> tariffPrices) {
        this.tariffPrices = tariffPrices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
