package domain;

import java.time.LocalDate;

/**
 * Created by Ihar_Niakhlebau on 17-Aug-15.
 */
public class SubscriberTariff {

    private int id;

    private Subscriber subscriber;

    private Tariff tariff;

    private LocalDate connectTariff;

    private LocalDate disconnectTariff;

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

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public LocalDate getConnectTariff() {
        return connectTariff;
    }

    public void setConnectTariff(LocalDate connectTariff) {
        this.connectTariff = connectTariff;
    }

    public LocalDate getDisconnectTariff() {
        return disconnectTariff;
    }

    public void setDisconnectTariff(LocalDate disconnectTariff) {
        this.disconnectTariff = disconnectTariff;
    }
}
