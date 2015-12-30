package org.lostfan.ktv.view.components;

import org.lostfan.ktv.controller.EntityOneController;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.AdditionalServiceEntityView;
import org.lostfan.ktv.view.ChangeOfTariffEntityView;
import org.lostfan.ktv.view.ConnectionEntityView;
import org.lostfan.ktv.view.DisconnectionEntityView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.RenderedServiceEntityView;

public class EntityViewFactory {

    public static EntityView createRenderedServiceForm(RenderedServiceEntityModel renderedServiceEntityModel, FixedServices service) {

        if(service == FixedServices.CONNECTION) {
            ConnectionEntityView entityView = new ConnectionEntityView(renderedServiceEntityModel);
            return entityView;
        }

        if(service == FixedServices.DISCONNECTION) {
            DisconnectionEntityView entityView = new DisconnectionEntityView(renderedServiceEntityModel);
            return entityView;
        }

        if(service == FixedServices.CHANGE_OF_TARIFF) {
            ChangeOfTariffEntityView entityView = new ChangeOfTariffEntityView(renderedServiceEntityModel);
            return entityView;
        }

        if(service == FixedServices.ADDITIONAL_SERVICE) {
            AdditionalServiceEntityView entityView = new AdditionalServiceEntityView(renderedServiceEntityModel);
            return entityView;
        }

        throw new IllegalArgumentException("Wrong model.");
    }

    public static EntityView createRenderedServiceForm(RenderedServiceEntityModel renderedServiceEntityModel, RenderedService renderedService) {
        if (renderedServiceEntityModel == null) {
            throw new IllegalArgumentException("Wrong model.");
        }

        if(renderedService.getServiceId() == FixedServices.CONNECTION.getId()) {
            ConnectionEntityView entityView = new ConnectionEntityView(renderedServiceEntityModel, renderedServiceEntityModel.getConnectionRenderedService(renderedService));
            return entityView;
        }

        if(renderedService.getServiceId() == FixedServices.DISCONNECTION.getId()) {
            DisconnectionEntityView entityView = new DisconnectionEntityView(renderedServiceEntityModel, renderedServiceEntityModel.getDisconnectionRenderedService(renderedService));
            return entityView;
        }
        if(renderedService.getServiceId() == FixedServices.CHANGE_OF_TARIFF.getId()) {
            ChangeOfTariffEntityView entityView = new ChangeOfTariffEntityView(renderedServiceEntityModel, renderedServiceEntityModel.getChangeOfTariffRenderedService(renderedService));
            return entityView;
        }

        AdditionalServiceEntityView entityView = new AdditionalServiceEntityView(renderedServiceEntityModel, renderedServiceEntityModel.getAdditionalRenderedService(renderedService));
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
