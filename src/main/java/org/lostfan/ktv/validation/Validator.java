package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Entity;

public interface Validator<E extends Entity> {

    default ValidationResult validate(E entity) {
        return validate(entity, ValidationResult.createEmpty());
    }

    ValidationResult validate(E entity, ValidationResult result);
}
