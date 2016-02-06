package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.TariffField;

public class RenderedServiceValidator implements Validator<RenderedService> {

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        if (entity.getDate() == null) {
            result.addError("errors.empty", "renderedService.date");
        }

        if (entity.getPrice() == null) {
            result.addError("errors.empty", "renderedService.price");
        }

        if (entity.getServiceId() == null) {
            result.addError("errors.empty", "service");
        }

        if (entity.getSubscriberAccount() == null) {
            result.addError("errors.empty", "subscriber");
        }
        if(entity instanceof TariffField) {
            if(((TariffField) entity).getTariffId() == null) {
                result.addError("errors.empty", "tariff");
            }
        }

        return result;
    }
}
