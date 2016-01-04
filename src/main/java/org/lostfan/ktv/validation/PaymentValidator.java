package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Payment;

public class PaymentValidator implements Validator<Payment> {

    @Override
    public ValidationResult validate(Payment entity, ValidationResult result) {
        if (entity.getDate() == null) {
            result.addError("empty", "payment.payDate");
        }

        if (entity.getPrice() == null) {
            result.addError("empty", "payment.price");
        }

        if (entity.getServicePaymentId() == null) {
            result.addError("empty", "service");
        }

        if (entity.getSubscriberAccount() == null) {
            result.addError("empty", "subscriber");
        }

        return result;
    }
}
