package org.lostfan.ktv.model.transform;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.RenderedServiceExt;

public class PaymentTransformer implements EntityTransformer<Payment, PaymentExt> {

    @Override
    public PaymentExt transformTo(Payment entity) {
        PaymentExt dto = new PaymentExt();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setPrice(entity.getPrice());
        dto.setSubscriberAccount(entity.getSubscriberAccount());
        dto.setServicePaymentId(entity.getServicePaymentId());
        dto.setRenderedServicePaymentId(entity.getRenderedServicePaymentId());
        dto.setPaymentTypeId(entity.getPaymentTypeId());
        return dto;
    }
}
