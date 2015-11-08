package org.lostfan.ktv.domain;

/**
 * Created by Ihar_Niakhlebau on 23-Sep-15.
 */
public class PaymentType implements Entity {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
