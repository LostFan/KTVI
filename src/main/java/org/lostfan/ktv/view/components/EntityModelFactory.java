package org.lostfan.ktv.view.components;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.BaseEntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.model.MaterialEntityModel;
import org.lostfan.ktv.model.RenderedServiceEntityModel;
import org.lostfan.ktv.model.ServiceEntityModel;
import org.lostfan.ktv.model.StreetEntityModel;
import org.lostfan.ktv.model.SubscriberEntityModel;
import org.lostfan.ktv.view.EntityView;

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