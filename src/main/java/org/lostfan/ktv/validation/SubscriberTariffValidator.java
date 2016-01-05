package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.SubscriberTariff;

public class SubscriberTariffValidator implements Validator<SubscriberTariff> {

    //TODO
    @Override
    public ValidationResult validate(SubscriberTariff entity, ValidationResult result) {

        if (entity.getTariffId() == null) {
            result.addError("errors.empty", "tariff");
        }

//        if (entity.getConnectTariff() == null) {
//            result.addError("empty", "tariff");
//        }
//
//        if (entity.getSubscriberAccount() == null) {
//            result.addError("empty", "tariff");
//        }

        return result;
    }
}
