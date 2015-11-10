package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.MainView;

public class MainController {

    private MainView view;
    private MainModel model;

    private EntityController entityController;
    private EntityModel activeEntityModel;
    private EntityTableView tableView;

    public MainController(MainModel model, MainView view) {
        this.model = model;
        this.view = view;

        this.view.setEntityModelListener(newModel -> {
            if (activeEntityModel ==  newModel) {
                return;
            }

            if (entityController == null) {
                activeEntityModel = newModel;
                tableView = new EntityTableView(newModel);
                entityController = new EntityController(newModel, tableView);
                model.setContentPanel(tableView.getContentPanel());
            } else {
                activeEntityModel = newModel;
                tableView.setModel(newModel);
                entityController.setModel(newModel);
                model.setContentPanel(tableView.getContentPanel());
            }
        });
    }
}
