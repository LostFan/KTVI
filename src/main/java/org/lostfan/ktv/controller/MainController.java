package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.MainView;

public class MainController {

    private MainView view;
    private MainModel model;

    private EntityController entityController;
    private EntityTableView tableView;

    public MainController(MainModel model, MainView view) {
        this.model = model;
        this.view = view;

        this.view.setMenuActionListener(args -> {
            String code = (String) args;
            model.setCurrentModel(code);
        });

        this.model.addObserver(args -> {
            EntityModel newModel = (EntityModel) args;
            if (entityController == null) {
                tableView = new EntityTableView(newModel);
                entityController = new EntityController(newModel, tableView);
                view.setTableView(tableView);
            } else {
                tableView.setModel(newModel);
                entityController.setModel(newModel);
                view.setTableView(tableView);
            }
        });
    }
}
