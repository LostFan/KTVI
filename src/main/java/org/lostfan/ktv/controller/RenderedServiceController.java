package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.RenderedServiceTableView;
import org.lostfan.ktv.view.components.EntityViewFactory;

public class RenderedServiceController extends EntityController {

    private RenderedServiceEntityModel model;

    public RenderedServiceController(RenderedServiceEntityModel model, RenderedServiceTableView view) {
        super(model, view);
        view.setConnectionActionListener(this::addConnectionActionPerformed);
        view.setDisconnectionActionListener(this::addDisconnectionActionPerformed);
        view.setChangeOfTariffActionListener(this::addChangeOfTariffActionPerformed);
        this.model = model;
    }


    private void addConnectionActionPerformed(Object args) {
        EntityViewFactory.createRenderedServiceForm(model, FixedServices.CONNECTION);
    }

    private void addDisconnectionActionPerformed(Object args) {
        EntityViewFactory.createRenderedServiceForm(model, FixedServices.DISCONNECTION);
    }

    private void addChangeOfTariffActionPerformed(Object args) {
        EntityViewFactory.createRenderedServiceForm(model, FixedServices.CHANGE_OF_TARIFF);
    }

    @Override
    protected void changeActionPerformed(Object args) {
        int selectedId = (Integer) args;

        RenderedService entity = model.getEntity(selectedId);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, entity);
    }
}
