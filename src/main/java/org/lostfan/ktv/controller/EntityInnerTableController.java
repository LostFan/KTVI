package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.view.EntityInnerTableView;
import org.lostfan.ktv.view.EntityView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Created by 1 on 07.11.2015.
 */
public class EntityInnerTableController {
    private EntityModel model;
    private EntityInnerTableView view;

    public EntityInnerTableController(EntityModel model, EntityInnerTableView view) {
        this.model = model;
        this.view = view;

        this.view.addAddActionListener(new AddActionListener());
    }

    private class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            EntityView entityView = new EntityView(model);
//            entityView.addAddActionListener(e1 -> {
//                Map<String, Object> values = entityView.getValues();
//                model.saveOrEditEntity(values);
//            });
        }
    }
}
