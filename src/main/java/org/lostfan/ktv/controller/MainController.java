package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.AdditionalRenderedService;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.model.entity.BaseModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.*;
import org.lostfan.ktv.view.components.EntityViewFactory;

import java.util.HashMap;
import java.util.Map;

public class MainController {

    private MainView view;
    private MainModel model;

    private Map<String, MainInnerController> innerControllers;

    public MainController(MainModel model, MainView view) {
        this.model = model;
        this.view = view;

        innerControllers = new HashMap<>();
        this.model.getEntityItems().forEach(this::createEntityController);
        this.model.getDocumentItems().forEach(this::createEntityController);

        this.view.setMenuEntityActionListener(args -> {
            String code = (String) args;
            model.setCurrentModel(code);
        });

        this.view.setMenuServiceActionListener(args -> {
            String code = (String) args;
            RenderedServiceEntityModel renderedServiceModel = MainModel.getRenderedServiceEntityModel();
            EntityView entityView = EntityViewFactory.createRenderedServiceForm(renderedServiceModel, FixedServices.of(code));
            entityView.setAddActionListener(args_ -> {
                ValidationResult result = ValidationResult.createEmpty();
                if (FixedServices.of(code) == FixedServices.CONNECTION) {
                    ConnectionRenderedService entity = (ConnectionRenderedService) args_;
                    result = renderedServiceModel.save(entity);
                }
                if (FixedServices.of(code) == FixedServices.DISCONNECTION) {
                    DisconnectionRenderedService entity = (DisconnectionRenderedService) args_;
                    result = renderedServiceModel.save(entity);
                }
                if (FixedServices.of(code) == FixedServices.CHANGE_OF_TARIFF) {
                    ChangeOfTariffRenderedService entity = (ChangeOfTariffRenderedService) args_;
                    result = renderedServiceModel.save(entity);
                }
                if (FixedServices.of(code) == FixedServices.ADDITIONAL_SERVICE) {
                    AdditionalRenderedService entity = (AdditionalRenderedService) args_;
                    result = renderedServiceModel.save(entity);
                }
                if (result.hasErrors()) {
                    entityView.showErrors(result.getErrors());
                    return;
                }
                entityView.hide();
            });
        });

        this.model.addObserver(args -> {
            BaseModel currentModel = (BaseModel) args;
            MainInnerController currentController = this.innerControllers.get(currentModel.getEntityNameKey());
            view.setInnerView(currentController.getView());
        });
    }

    private void createEntityController(String entityCode) {
        MainInnerController entityController;
        if (entityCode.equals(MainModel.getRenderedServiceEntityModel().getEntityNameKey())) {
            entityController = new RenderedServiceController(MainModel.getRenderedServiceEntityModel(),
                    new RenderedServiceTableView(MainModel.getRenderedServiceEntityModel()));
        } else if (entityCode.equals(MainModel.getTariffEntityModel().getEntityNameKey())) {
            entityController = new TariffEntityController(MainModel.getTariffEntityModel(),
                    new TariffTableView(MainModel.getTariffEntityModel()));
        } else if (entityCode.equals(MainModel.getSubscriptionFeeModel().getEntityNameKey())) {
            entityController = new SubscriptionFeeController(MainModel.getSubscriptionFeeModel(),
                    new SubscriptionFeeTableView(MainModel.getSubscriptionFeeModel()));
        }
        else {
            EntityModel model = MainModel.getEntityModel(entityCode);
            entityController = new EntityController(model, new EntityTableView(model));
        }
        this.innerControllers.put(entityCode, entityController);
    }
}
