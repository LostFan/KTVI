package org.lostfan.ktv.view.components;

import org.lostfan.ktv.model.entity.BaseEntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.MaterialEntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.model.entity.ServiceEntityModel;
import org.lostfan.ktv.model.entity.StreetEntityModel;
import org.lostfan.ktv.model.entity.SubscriberEntityModel;

public class EntityModelFactory {

    public static EntityModel createForm(EntityFieldTypes type) {
        BaseEntityModel model = null;
        switch (type) {
            case Street:
                model = new StreetEntityModel();
                break;
            case Service:
                model = new ServiceEntityModel();
                break;
            case Subscriber:
                model = new SubscriberEntityModel();
                break;
            case Material:
                model = new MaterialEntityModel();
                break;
            case RenderedService:
                model = new RenderedServiceEntityModel();
                break;
            default:
                throw new IllegalArgumentException("Wrong type: " + type.toString());
        }
        return model;
    }
}
