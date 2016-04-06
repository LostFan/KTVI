package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.view.entity.SubscriberInformationView;
import org.lostfan.ktv.view.table.SubscriberTableView;

public class SubscriberController extends EntityController {

    private SubscriberEntityModel model;

    public SubscriberController(SubscriberEntityModel model, SubscriberTableView view) {
        super(model, view);
        this.model = model;
        view.setFindActionListener(this::findActionPerformed);
        view.setBalanceActionListener(this::balanceActionPerformed);
    }

    private void findActionPerformed(Object args) {
        String query = (String) args;
        this.model.setSearchQuery(query);
    }

    private void balanceActionPerformed(Object args) {
        Integer subscriberId = (Integer) args;
        SubscriberInformationView subscriberInformationView = new SubscriberInformationView(model, model.getEntity(subscriberId));
    }
}
