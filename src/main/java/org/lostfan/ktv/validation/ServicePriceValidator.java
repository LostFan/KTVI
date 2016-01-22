package org.lostfan.ktv.validation;

import java.time.LocalDate;

import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.domain.TariffPrice;

public class ServicePriceValidator implements Validator<ServicePrice> {

    @Override
    public ValidationResult validate(ServicePrice entity, ValidationResult result) {

        // entity.serviceId is expected always to be set

        if (entity.getDate() != null) {
            if (entity.getPrice() < 0) {
                result.addError("errors.negative", "servicePrice.price");
            }

            if (!LocalDate.now().isBefore(entity.getDate())) {
                result.addError("errors.pastDate", "servicePrice.date");
            }
        } else {
            result.addError("errors.pastDate", "servicePrice.date");
        }

        return result;
    }
}
