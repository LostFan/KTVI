package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.view.SubscriberTableView;

public class SubscriberController extends EntityController {

    SubscriberEntityModel model;

    public SubscriberController(SubscriberEntityModel model, SubscriberTableView view) {
        super(model, view);
        this.model = model;
        view.setFindActionListener(this::findActionPerformed);
    }

    private void findActionPerformed(Object args) {
        String query = (String) args;
        this.model.setSearchQuery(query);
    }
}
