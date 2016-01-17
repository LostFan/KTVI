package org.lostfan.ktv.view.components;

import org.lostfan.ktv.model.searcher.EntitySearcherModel;

public class EntityComboBoxController {

    private EntitySearcherModel model;
    private EntityComboBox view;

    public EntityComboBoxController(EntitySearcherModel model, EntityComboBox view) {
        this.model = model;
        this.view = view;
        this.view.setSearchActionListener(this::searchAction);
    }

    private void searchAction(Object args) {
        String query = args.toString();
        model.setSearchQuery(query);
    }
}
