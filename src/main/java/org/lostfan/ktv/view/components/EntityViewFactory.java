package org.lostfan.ktv.view.components;

import org.lostfan.ktv.controller.EntityOneController;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.view.EntityView;

public class EntityViewFactory {

    public static EntityView createForm(EntityFieldTypes type) {
        EntityModel model = MainModel.getEntityModel(type.getValueClass());
        if (model == null) {
            throw new IllegalArgumentException("Wrong type: " + type.toString());
        }

        return new EntityView(model);
    }

    public static EntityView createForm(EntityFieldTypes type, Integer id) {
        if(id == null) {
            return createForm(type);
        }
        EntityModel model = MainModel.getEntityModel(type.getValueClass());
        if (model == null) {
            throw new IllegalArgumentException("Wrong type: " + type.toString());
        }

        EntityView entityView = new EntityView(model, model.getEntity(id));
        new EntityOneController(model, entityView);
        return entityView;
    }
}
