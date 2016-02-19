package org.lostfan.ktv.model;

public enum FixedServices {
    CONNECTION (2, "connection"),
    RECONNECTION (3, "reconnection"),
    DISCONNECTION (4, "disconnection"),
    CHANGE_OF_TARIFF (5, "changeOfTariff"),
    SUBSCRIPTION_FEE (1, "subscriptionFee"),
    ADDITIONAL_SERVICE("additionalService"),
    MATERIALS(7,"materials");

    public static FixedServices of(String code) {
        for (FixedServices service : FixedServices.values()) {
            if (service.code.equals(code)) {
                return service;
            }
        }
        return null;
    }

    private final int id;

    private final String code;

    FixedServices(int id, String code) {
        this.id = id;
        this.code = code;
    }

    FixedServices(String code) {
        id = 0;
        this.code = code;
    }

    public int getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }
}
