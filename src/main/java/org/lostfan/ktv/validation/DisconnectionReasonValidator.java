package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.DisconnectionReason;

public class DisconnectionReasonValidator implements Validator<DisconnectionReason> {

    @Override
    public ValidationResult validate(DisconnectionReason disconnectionReason, ValidationResult result) {

        if (disconnectionReason.getName() == null || disconnectionReason.getName().length() == 0) {
            result.addError("errors.empty", "disconnectionReason.name");
        }

        return result;
    }
}
