package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Payment;

public class PaymentValidator implements Validator<Payment> {

    @Override
    public ValidationResult validate(Payment entity, ValidationResult result) {
        if (entity.getDate() == null) {
            result.addError("errors.empty", "payment.payDate");
        }

        if (entity.getPrice() == null) {
            result.addError("errors.empty", "payment.price");
        }

        if (entity.getRenderedServicePaymentId() == null) {
            result.addError("errors.empty", "service");
        }

        if (entity.getSubscriberAccount() == null) {
            result.addError("errors.empty", "subscriber");
        }

        return result;
    }
}
