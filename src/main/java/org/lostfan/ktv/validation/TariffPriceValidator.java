package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.TariffPrice;

import java.time.LocalDate;

public class TariffPriceValidator implements Validator<TariffPrice> {

    @Override
    public ValidationResult validate(TariffPrice entity, ValidationResult result) {

        // entity.tariffId is expected always to be set

        if (entity.getDate() != null) {
            if (entity.getPrice() < 0) {
                result.addError("errors.negative", "tariffPrice.price");
            }

            if (!LocalDate.now().isBefore(entity.getDate())) {
                result.addError("errors.pastDate", "tariffPrice.date");
            }
        }

        return result;
    }
}
