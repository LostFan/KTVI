package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.components.EntityViewFactory;

public class RenderedServiceController extends EntityController {

    private RenderedServiceEntityModel model;

    public RenderedServiceController(RenderedServiceEntityModel model, EntityTableView view) {
        super(model, view);
        this.model = model;
    }

    @Override
    protected void changeActionPerformed(Object args) {
        int selectedId = (Integer) args;

        RenderedService entity = model.getEntity(selectedId);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, entity);
    }
}
