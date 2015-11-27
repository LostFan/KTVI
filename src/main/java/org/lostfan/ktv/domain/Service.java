package org.lostfan.ktv.domain;

public class Service extends DefaultEntity  {

    private Integer id;

    private String name;

    private boolean additionalService;

    private boolean isConsumeMaterials;

    private boolean isChangeTariff;

    private boolean isConnectionService;

    private boolean isDisconnectionService;

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

    public Integer getId() {return id;}

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isConsumeMaterials() {
        return isConsumeMaterials;
    }

    public void setConsumeMaterials(boolean consumeMaterials) {
        isConsumeMaterials = consumeMaterials;
    }

    public boolean isChangeTariff() {
        return isChangeTariff;
    }

    public void setChangeTariff(boolean changeTariff) {
        isChangeTariff = changeTariff;
    }

    public boolean isConnectionService() {
        return isConnectionService;
    }

    public void setConnectionService(boolean connectionService) {
        isConnectionService = connectionService;
    }

    public boolean isDisconnectionService() {
        return isDisconnectionService;
    }

    public void setDisconnectionService(boolean disconnectionService) {
        isDisconnectionService = disconnectionService;
    }
}
