package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Subscriber;

public class SubscriberValidator implements Validator<Subscriber> {

    @Override
    public ValidationResult validate(Subscriber entity, ValidationResult result) {

        if (entity.getName() == null || entity.getName().length() == 0) {
            result.addError("empty", "subscriber.name");
        }

        if (entity.getStreetId() == null) {
            result.addError("empty", "subscriber.street_id");
        }

        if (entity.getAccount() == null || entity.getAccount() == 0) {
            result.addError("empty", "subscriber.account");
        }

        return result;
    }
}
