package org.lostfan.ktv.model;

public enum FixedServices {
    CONNECTION (1, "connection");

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

    public int getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }
}
