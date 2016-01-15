package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.Service;

public class PaymentExt extends Payment {

    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
