package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Entity;

public interface Validator<E extends Entity> {

    ValidationResult validate(E entity);
}
