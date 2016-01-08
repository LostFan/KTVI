package org.lostfan.ktv.validation;

import org.lostfan.ktv.domain.Material;

public class MaterialValidator implements Validator<Material> {

    @Override
    public ValidationResult validate(Material entity, ValidationResult result) {
        if (entity.getName() == null || entity.getName().length() == 0) {
            result.addError("errors.empty", "material.name");
        }
        if (entity.getPrice() == null) {
            result.addError("errors.empty", "material.price");
        }
        if (entity.getUnit() == null || entity.getUnit().length() == 0) {
            result.addError("errors.empty", "material.unit");
        }
        return result;
    }
}
