package org.lostfan.ktv.model;

public enum FixedServices {
    CONNECTION (1, "connection");

    private final int id;

    private final String name;

    private FixedServices(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
