package domain;

import java.time.LocalDate;

/**
 * Created by Ihar_Niakhlebau on 17-Aug-15.
 */
public class ServicePrice {

    private int id;

    private Service service;

    private int price;

    private LocalDate date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
