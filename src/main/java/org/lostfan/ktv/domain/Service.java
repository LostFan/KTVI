package org.lostfan.ktv.domain;

import java.util.List;

public class Service implements Entity  {

    private Integer id;

    private String name;

    private boolean additionalService;

    private List<ServicePrice> servicePrices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdditionalService() {
        return additionalService;
    }

    public void setAdditionalService(boolean additionalService) {
        this.additionalService = additionalService;
    }

    public List<ServicePrice> getServicePrices() {
        return servicePrices;
    }

    public void setServicePrices(List<ServicePrice> servicePrices) {
        this.servicePrices = servicePrices;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {
        this.id = id;
    }
}
