package org.lostfan.ktv.model;

public enum PaymentTypes {
    POST(1, "post"),
    BANK(2, "bank");

    public static PaymentTypes of(String code) {
        for (PaymentTypes service : PaymentTypes.values()) {
            if (service.code.equals(code)) {
                return service;
            }
        }
        return null;
    }

    private final int id;

    private final String code;

    PaymentTypes(int id, String code) {
        this.id = id;
        this.code = code;
    }

    PaymentTypes(String code) {
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
