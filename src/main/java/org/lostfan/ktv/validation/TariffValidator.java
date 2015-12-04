package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Tariff;

public class TariffValidator implements Validator<Tariff> {

    @Override
    public ValidationResult validate(Tariff entity, ValidationResult result) {

        if (entity.getName() == null || entity.getName().length() == 0) {
            result.addError("empty", "tariff.name");
        }

        return result;
    }
}
