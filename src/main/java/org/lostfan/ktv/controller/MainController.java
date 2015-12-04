package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.MainView;
import org.lostfan.ktv.view.RenderedServiceTableView;
import org.lostfan.ktv.view.components.EntityViewFactory;

public class MainController {

    private MainView view;
    private MainModel model;

    private EntityController entityController;
    private RenderedServiceController renderedServiceController;
    private EntityTableView tableView;
    private RenderedServiceTableView renderedServiceTableView;

    public MainController(MainModel model, MainView view) {
        this.model = model;
        this.view = view;

        this.view.setMenuEntityActionListener(args -> {
            String code = (String) args;
            model.setCurrentModel(code);
        });

        this.view.setMenuServiceActionListener(args -> {
            String code = (String) args;
            RenderedServiceEntityModel renderedServiceModel =MainModel.getRenderedServiceEntityModel();
            EntityView entityView = EntityViewFactory.createRenderedServiceForm(renderedServiceModel, FixedServices.of(code));
            entityView.setAddActionListener(args_ -> {
                RenderedService entity = (RenderedService) args_;
                ValidationResult result = renderedServiceModel.save(entity);
                if (result.hasErrors()) {
                    entityView.showErrors(result.getErrors());
                    return;
                }
                renderedServiceModel.save(entity);
                entityView.hide();
            });
        });

        this.model.addObserver(args -> {
            if (args instanceof RenderedServiceEntityModel) {
                RenderedServiceEntityModel newModel = (RenderedServiceEntityModel) args;
                if (renderedServiceController == null) {
                    renderedServiceTableView = new RenderedServiceTableView(newModel);
                    renderedServiceController = new RenderedServiceController(newModel, renderedServiceTableView);
                    view.setTableView(renderedServiceTableView);
                } else {
                    renderedServiceTableView.setModel(newModel);
                    renderedServiceController.setModel(newModel);
                    view.setTableView(renderedServiceTableView);
                }
                return;
            }
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
