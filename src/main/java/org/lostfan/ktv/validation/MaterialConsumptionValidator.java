package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.MaterialConsumption;

public class MaterialConsumptionValidator implements Validator<MaterialConsumption> {

    @Override
    public ValidationResult validate(MaterialConsumption entity, ValidationResult result) {

        if (entity.getMaterialId() == null || entity.getMaterialId().toString().length() == 0) {
            result.addError("empty", "material");
        }

        if (entity.getAmount() == null|| entity.getAmount().toString().length() == 0) {
            result.addError("empty", "materialConsumption.amount");
        }

//        if (entity.getAccount() == null || entity.getAccount().length() == 0) {
//            result.addError("empty", "subscriber.account");
//        }

        return result;
    }
}
