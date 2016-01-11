package org.lostfan.ktv.domain;


import java.time.LocalDate;

public class Subscriber extends DefaultEntity {

    private Integer account;

    private String name;

    private Integer balance;

    private Boolean connected;

    private Integer streetId;

    private Integer house;

    private String building;

    private String flat;

    private String index;

    private String phone;

    private String passportNumber;

    private String passportAuthority;

    private LocalDate passportDate;

    private LocalDate contractDate;

    public Integer getId() {
        return account;
    }

    public void setId(Integer id) {
        this.account = id;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    public Integer getHouse() {
        return house;
    }

    public void setHouse(Integer house) {
        this.house = house;
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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportAuthority() {
        return passportAuthority;
    }

    public void setPassportAuthority(String passportAuthority) {
        this.passportAuthority = passportAuthority;
    }

    public LocalDate getPassportDate() {
        return passportDate;
    }

    public void setPassportDate(LocalDate passportDate) {
        this.passportDate = passportDate;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }
}
