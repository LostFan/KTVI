package org.lostfan.ktv.view.components;

import org.lostfan.ktv.controller.EntityOneController;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.BaseEntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.EntityTableModel;
import org.lostfan.ktv.model.entity.MaterialEntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.model.entity.ServiceEntityModel;
import org.lostfan.ktv.model.entity.StreetEntityModel;
import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.view.EntityView;

public class EntityViewFactory {

    public static EntityView createForm(EntityFieldTypes type) {
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
        return new EntityView(model);
    }

    public static EntityView createForm(EntityFieldTypes type, Integer id) {
        if(id == null) {
            return createForm(type);
        }
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
        EntityView entityView = new EntityView(model, model.getEntity(id));
        new EntityOneController(model, entityView);
        return entityView;
    }
}
