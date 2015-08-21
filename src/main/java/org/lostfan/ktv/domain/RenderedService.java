package org.lostfan.ktv.domain;

import java.time.LocalDate;
import java.util.List;

public class RenderedService {

    private int id;

    private Subscriber subscriber;

    private Service service;

    private List<MaterialConsumption> materials;

    private LocalDate date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public List<MaterialConsumption> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialConsumption> materials) {
        this.materials = materials;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
