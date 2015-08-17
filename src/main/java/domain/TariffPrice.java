package domain;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Ihar_Niakhlebau on 17-Aug-15.
 */
public class TariffPrice {

    private int id;

    private Tariff tariff;

    private int price;

    private LocalDate date;

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
