package org.lostfan.ktv.controller;

import java.util.List;
import java.util.Map;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;

public class EntityOneController {

    private EntityModel model;
    private EntityView view;

    public EntityOneController(EntityModel model, EntityView view) {
        this.model = model;
        this.view = view;

        this.view.setChangeActionListener(new ChangeActionListener());

    }

    public void setModel(EntityModel model) {
        this.model = model;
    }


    private class ChangeActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {
            view.setAddActionListener(args_ -> {
                Map<String, Object> values = view.getValues();
                model.saveOrEditEntity(values);
            });
        }
    }
}
