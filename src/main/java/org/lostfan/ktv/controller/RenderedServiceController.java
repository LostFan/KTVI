package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.*;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.ConnectionEntityView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.RenderedServiceTableView;
import org.lostfan.ktv.view.components.EntityViewFactory;

import java.time.LocalDate;
import java.util.List;

public class RenderedServiceController extends EntityController {

    private RenderedServiceEntityModel model;

    private RenderedServiceTableView view;

    public RenderedServiceController(RenderedServiceEntityModel model, RenderedServiceTableView view) {
        super(model, view);
        view.setConnectionActionListener(this::addConnectionActionPerformed);
        view.setReconnectionActionListener(this::addReconnectionActionPerformed);
        view.setDisconnectionActionListener(this::addDisconnectionActionPerformed);
        view.setChangeOfTariffActionListener(this::addChangeOfTariffActionPerformed);
        view.setAdditionalServiceActionListener(this::addAdditionalServiceActionPerformed);
        view.setMaterialsServiceActionListener(this::addMaterialsServiceActionPerformed);
        view.newDateActionListener(this::newDateActionPerformed);
        this.model = model;
        this.view = view;
    }


    private void addConnectionActionPerformed(Object args) {
        model.setServiceFieldEditable(false);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, FixedServices.CONNECTION);
        entityView.setAddActionListener(args_ -> {
            ConnectionRenderedService entity = (ConnectionRenderedService) args_;
            ValidationResult result = model.save(entity);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    private void addReconnectionActionPerformed(Object args) {
        model.setServiceFieldEditable(false);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, FixedServices.RECONNECTION);
        entityView.setAddActionListener(args_ -> {
            ReconnectionRenderedService entity = (ReconnectionRenderedService) args_;
            ValidationResult result = model.save(entity);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    private void addDisconnectionActionPerformed(Object args) {
        model.setServiceFieldEditable(false);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, FixedServices.DISCONNECTION);
        entityView.setAddActionListener(args_ -> {
            DisconnectionRenderedService entity = (DisconnectionRenderedService) args_;
            ValidationResult result = model.save(entity);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    private void addChangeOfTariffActionPerformed(Object args) {
        model.setServiceFieldEditable(false);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, FixedServices.CHANGE_OF_TARIFF);
        entityView.setAddActionListener(args_ -> {
            ChangeOfTariffRenderedService entity = (ChangeOfTariffRenderedService) args_;
            ValidationResult result = model.save(entity);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    private void addAdditionalServiceActionPerformed(Object args) {
        model.setServiceFieldEditable(true);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, FixedServices.ADDITIONAL_SERVICE);
        entityView.setAddActionListener(args_ -> {
            RenderedService entity = (RenderedService) args_;
            ValidationResult result = model.save(entity);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    private void addMaterialsServiceActionPerformed(Object args) {
        model.setServiceFieldEditable(false);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, FixedServices.MATERIALS);
        entityView.setAddActionListener(args_ -> {
            MaterialsRenderedService entity = (MaterialsRenderedService) args_;
            ValidationResult result = model.save(entity);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    @Override
    protected void changeActionPerformed(Object args) {
        int selectedId = (Integer) args;

        RenderedService entity = model.getEntity(selectedId);
        EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, entity);
        entityView.setAddActionListener(args_ -> {
            ValidationResult result = ValidationResult.createEmpty();
            if(entity.getServiceId() == FixedServices.CONNECTION.getId()) {
                ConnectionRenderedService entity1 = (ConnectionRenderedService) args_;
                result = model.save(entity1);
            } else if(entity.getServiceId() == FixedServices.RECONNECTION.getId()) {
                ReconnectionRenderedService entity1 = (ReconnectionRenderedService) args_;
                result = model.save(entity1);
            } else if(entity.getServiceId() == FixedServices.DISCONNECTION.getId()) {
                DisconnectionRenderedService entity1 = (DisconnectionRenderedService) args_;
                result = model.save(entity1);
            } else if(entity.getServiceId() == FixedServices.CHANGE_OF_TARIFF.getId()) {
                ChangeOfTariffRenderedService entity1 = (ChangeOfTariffRenderedService) args_;
                result = model.save(entity1);
            } else if(entity.getServiceId() == FixedServices.MATERIALS.getId()) {
                MaterialsRenderedService entity1 = (MaterialsRenderedService) args_;
                result = model.save(entity1);
            } else {
                RenderedService entity1 = (RenderedService) args_;
                result = model.save(entity1);
            }
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    private void newDateActionPerformed(Object args) {
        LocalDate date = (LocalDate) args;
        this.model.setDate(date);
    }

    protected void deleteActionPerformed(Object args) {
        List<Integer> selectedIds = (List<Integer>) args;
        ValidationResult result = this.model.deleteRenderedServicesById(selectedIds);
        if (result.hasErrors()) {
            view.errorWindow(result.getErrors());
        }
    }
}
