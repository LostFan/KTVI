package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Service;

public class ServiceValidator implements Validator<Service> {

    @Override
    public ValidationResult validate(Service service, ValidationResult result) {

        if (service.getName() == null || service.getName().length() == 0) {
            result.addError("errors.empty", "service.name");
        }

        return result;
    }
}
