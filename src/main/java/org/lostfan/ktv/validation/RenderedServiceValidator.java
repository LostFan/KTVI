package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.RenderedService;

public class RenderedServiceValidator implements Validator<RenderedService> {
//
//    @Override
//    public ValidationResult validate(RenderedService entity) {
//        ValidationResult result = ValidationResult.createEmpty();
//
//
//    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        if (entity.getDate() == null) {
            result.addError("empty", "renderedService.date");
        }

        if (entity.getPrice() == null) {
            result.addError("empty", "renderedService.price");
        }

        if (entity.getServiceId() == null) {
            result.addError("empty", "service");
        }

        if (entity.getSubscriberId() == null) {
            result.addError("empty", "subscriber");
        }

        return result;
    }
}
