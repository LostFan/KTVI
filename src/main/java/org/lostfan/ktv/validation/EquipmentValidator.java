package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Equipment;

public class EquipmentValidator implements Validator<Equipment> {

    @Override
    public ValidationResult validate(Equipment entity, ValidationResult result) {
        if (entity.getName() == null || entity.getName().length() == 0) {
            result.addError("errors.empty", "equipment.name");
        }
        if (entity.getPrice() == null) {
            result.addError("errors.empty", "equipment.price");
        }
        return result;
    }
}
