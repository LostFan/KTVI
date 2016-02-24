package org.lostfan.ktv.domain;

public class PaymentType extends DefaultEntity {

    private Integer id;

    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
