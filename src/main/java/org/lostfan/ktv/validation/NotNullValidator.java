package org.lostfan.ktv.validation;

public class NotNullValidator {

    public ValidationResult validate(Object entity, String fieldName, ValidationResult result) {
        if (entity == null) {
            result.addError("errors.empty", fieldName);
        }
        return result;
    }

}
