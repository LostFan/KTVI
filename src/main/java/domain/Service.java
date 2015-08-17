package domain;

import java.util.List;

public class Service {

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
}
