package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Street;

public class StreetValidator implements Validator<Street> {

    @Override
    public ValidationResult validate(Street street, ValidationResult result) {

        if (street.getName() == null || street.getName().length() == 0) {
            result.addError("errors.empty", "street.name");
        }

        return result;
    }
}
