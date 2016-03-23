package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Subscriber;

public class PaymentExt extends Payment {

    private Service service;

    private Subscriber subscriber;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}
