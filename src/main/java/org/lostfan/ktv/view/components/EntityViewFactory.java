package org.lostfan.ktv.view.components;

import org.lostfan.ktv.controller.EntityOneController;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.ConnectionEntityView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.RenderedServiceEntityView;

public class EntityViewFactory {

    public static EntityView createRenderedServiceForm(RenderedServiceEntityModel renderedServiceEntityModel, String code) {
        if (renderedServiceEntityModel == null) {
            throw new IllegalArgumentException("Wrong model.");
        }
        EntityView entityView = null;
        if(code.equals(FixedServices.CONNECTION.getName())) {
            entityView = new ConnectionEntityView(renderedServiceEntityModel);
        }
        return entityView;
    }

    public static EntityView createRenderedServiceForm(RenderedServiceEntityModel renderedServiceEntityModel, RenderedService renderedService) {
        if (renderedServiceEntityModel == null) {
            throw new IllegalArgumentException("Wrong model.");
        }
        EntityView entityView = null;
        if(renderedService.getServiceId() == FixedServices.CONNECTION.getId()) {

            entityView = new ConnectionEntityView(renderedServiceEntityModel, renderedServiceEntityModel.getConnectionRenderedService(renderedService));
        }
        return entityView;
    }

    public static EntityView createForm(EntityFieldTypes type) {
        EntityModel model = MainModel.getEntityModel(type.getValueClass());
        if (model == null) {
            throw new IllegalArgumentException("Wrong type: " + type.toString());
        }
        EntityView entityView = null;
        switch (type) {
            case RenderedService:
                entityView = new RenderedServiceEntityView(model);
                break;
            default:
                entityView = new EntityView(model);
        }
        return entityView;
    }

    public static EntityView createForm(EntityFieldTypes type, Integer id) {
        if(id == null) {
            return createForm(type);
        }
        EntityModel model = MainModel.getEntityModel(type.getValueClass());
        if (model == null) {
            throw new IllegalArgumentException("Wrong type: " + type.toString());
        }
        EntityView entityView = null;
        switch (type) {
            case RenderedService:
                entityView = new RenderedServiceEntityView(model, model.getEntity(id));
                break;
            default:
                entityView = new EntityView(model, model.getEntity(id));
        }
        new EntityOneController(model, entityView);
        return entityView;
    }

    public static EntityView createForm(EntityModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Empty model");
        }
        if (model.getEntityClass() == RenderedService.class) {
            return new RenderedServiceEntityView(model);
        }
        return new EntityView(model);
    }

    public static EntityView createForm(EntityModel model, Entity entity) {
        if(entity == null) {
            return createForm(model);
        }
        if (model == null) {
            throw new IllegalArgumentException("Empty model");
        }
        EntityView entityView = null;
        if (model.getEntityClass() == RenderedService.class) {
            entityView = new RenderedServiceEntityView(model, entity);
        } else {
            entityView = new EntityView(model, entity);
        }
        new EntityOneController(model, entityView);
        return entityView;
    }
}
