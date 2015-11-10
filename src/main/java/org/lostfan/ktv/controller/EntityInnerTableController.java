package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.EntityInnerTableView;

public class EntityInnerTableController {

    private EntityModel model;
    private EntityInnerTableView view;

    public EntityInnerTableController(EntityModel model, EntityInnerTableView view) {
        this.model = model;
        this.view = view;

        this.view.setAddActionListener(new AddActionListener());
    }

    private class AddActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {
//            EntityView entityView = new EntityView(model);
//            entityView.addAddActionListener(e1 -> {
//                Map<String, Object> values = entityView.getValues();
//                model.saveOrEditEntity(values);
//            });
        }
    }
}
