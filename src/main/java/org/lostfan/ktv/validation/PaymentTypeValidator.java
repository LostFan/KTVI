package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.PaymentType;

public class PaymentTypeValidator implements Validator<PaymentType> {

    @Override
    public ValidationResult validate(PaymentType paymentType, ValidationResult result) {

        if (paymentType.getName() == null || paymentType.getName().length() == 0) {
            result.addError("errors.empty", "paymentType.name");
        }

        return result;
    }
}
