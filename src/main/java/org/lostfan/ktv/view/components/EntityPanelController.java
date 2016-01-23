package org.lostfan.ktv.view.components;

import org.lostfan.ktv.model.searcher.EntitySearcherModel;

public class EntityPanelController {

    private EntitySearcherModel model;
    private EntityPanel view;

    public EntityPanelController(EntitySearcherModel model, EntityPanel view) {
        this.model = model;
        this.view = view;
        this.view.setSearchActionListener(this::searchAction);
    }

    private void searchAction(Object args) {
        String query = args.toString();
        model.setSearchQuery(query);
    }
}
