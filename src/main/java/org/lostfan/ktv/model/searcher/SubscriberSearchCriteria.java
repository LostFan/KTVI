package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

public class SubscriberSearchCriteria {

    private Integer account;
    private List<String> nameIn;
    private List<String> streetIn;
    private Integer house;
    private String index;
    private String building;
    private String flat;

    public SubscriberSearchCriteria() {
        this.nameIn = new ArrayList<>();
        this.streetIn = new ArrayList<>();
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public List<String> getNameIn() {
        return nameIn;
    }

    public void setNameIn(List<String> nameIn) {
        this.nameIn = nameIn;
    }

    public void addName(String name) {
        this.nameIn.add(name);
    }

    public List<String> getStreetIn() {
        return streetIn;
    }

    public void addStreet(String street) {
        this.streetIn.add(street);
    }

    public void setStreetIn(List<String> streetIn) {
        this.streetIn = streetIn;
    }

    public Integer getHouse() {
        return house;
    }

    public void setHouse(Integer house) {
        this.house = house;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }
}
