package org.lostfan.ktv.model;


public enum Reports {
    TURNOVER_SHEET("turnoverSheet");

    private final String code;

    Reports(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
