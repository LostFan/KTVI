package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.MainView;
import org.lostfan.ktv.view.RenderedServiceTableView;
import org.lostfan.ktv.view.TariffTableView;
import org.lostfan.ktv.view.components.EntityViewFactory;

import java.util.HashMap;
import java.util.Map;

public class MainController {

    private MainView view;
    private MainModel model;

    private Map<String, EntityController> entityControllers;

    public MainController(MainModel model, MainView view) {
        this.model = model;
        this.view = view;

        entityControllers = new HashMap<>();
        this.model.getEntityItems().forEach(this::createEntityController);
        this.model.getDocumentItems().forEach(this::createEntityController);

        this.view.setMenuEntityActionListener(args -> {
            String code = (String) args;
            model.setCurrentModel(code);
        });

        this.view.setMenuServiceActionListener(args -> {
            String code = (String) args;
            RenderedServiceEntityModel renderedServiceModel = MainModel.getRenderedServiceEntityModel();
            EntityViewFactory.createRenderedServiceForm(renderedServiceModel, FixedServices.of(code));
        });

        this.model.addObserver(args -> {
            EntityModel currentModel = (EntityModel) args;
            EntityController currentController = this.entityControllers.get(currentModel.getEntityNameKey());
            view.setTableView(currentController.getView());
        });
    }

    private void createEntityController(String entityCode) {
        EntityController entityController;
        if (entityCode.equals(MainModel.getRenderedServiceEntityModel().getEntityNameKey())) {
            entityController = new RenderedServiceController(MainModel.getRenderedServiceEntityModel(),
                    new RenderedServiceTableView(MainModel.getRenderedServiceEntityModel()));
        } else if (entityCode.equals(MainModel.getTariffEntityModel().getEntityNameKey())) {
            entityController = new TariffEntityController(MainModel.getTariffEntityModel(),
                    new TariffTableView(MainModel.getTariffEntityModel()));
        }
        else {
            EntityModel model = MainModel.getEntityModel(entityCode);
            entityController = new EntityController(model, new EntityTableView(model));
        }
        this.entityControllers.put(entityCode, entityController);
    }
}
