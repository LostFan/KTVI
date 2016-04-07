package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.MaterialConsumption;

public class MaterialConsumptionValidator implements Validator<MaterialConsumption> {

    @Override
    public ValidationResult validate(MaterialConsumption entity, ValidationResult result) {

        if (entity.getMaterialId() == null || entity.getMaterialId().toString().length() == 0) {
            result.addError("errors.empty", "material");
        }

        if (entity.getAmount() == null|| entity.getAmount().toString().length() == 0) {
            result.addError("errors.empty", "materialConsumption.amount");
        }


        return result;
    }
}
