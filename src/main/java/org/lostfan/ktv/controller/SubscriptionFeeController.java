package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.SubscriptionFeeRecalculationModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.entity.LoadRenderedServicesView;
import org.lostfan.ktv.view.table.SubscriptionFeeTableView;
import org.lostfan.ktv.view.entity.SubscriptionFeeView;

import java.time.LocalDate;
import java.util.List;

public class SubscriptionFeeController extends EntityController{

    private SubscriptionFeeModel model;
    private SubscriptionFeeTableView view;

    public SubscriptionFeeController(SubscriptionFeeModel model, SubscriptionFeeTableView view) {
        super(model, view);
        this.model = model;
        this.view = view;
        view.setDeleteForMonthActionListener(this::deleteForMonthActionPerformed);
        view.setLoadctionListener(this::loadActionPerformed);
        view.newDateActionListener(this::newDateActionPerformed);
    }

    private void loadActionPerformed(Object args) {
        RenderedServiceEntityModel renderedServiceEntityModel = MainModel.getRenderedServiceEntityModel();
        LoadRenderedServicesView loadRenderedServicesViewView = new LoadRenderedServicesView(renderedServiceEntityModel);
        loadRenderedServicesViewView.setAddActionListener(args_ -> {
            List<RenderedService> payments = (List<RenderedService>) args_;
            savePayments(renderedServiceEntityModel, payments, loadRenderedServicesViewView);
        });

        loadRenderedServicesViewView.setLoadRenderedServiceFileActionListener(args_ -> {
            List<RenderedService> renderedServices = (List<RenderedService>) args_;
            renderedServiceEntityModel.createRenderedServices(renderedServices);
        });
    }

    protected void deleteForMonthActionPerformed(Object args) {
        LocalDate date = (LocalDate) args;
        ValidationResult result = model.deleteForMonth(date);
        if (result.hasErrors()) {
            view.errorWindow(result.getErrors());
        }
    }

    private void newDateActionPerformed(Object args) {
        LocalDate date = (LocalDate) args;
        this.model.setDate(date);
    }

    @Override
    protected void deleteActionPerformed(Object args) {
        List<Integer> selectedIds = (List<Integer>) args;
        ValidationResult result = model.deleteEntityById(selectedIds);
        if (result.hasErrors()) {
            view.errorWindow(result.getErrors());
        }
    }

    private void savePayments(RenderedServiceEntityModel model, List<RenderedService> payments, FormView view) {
        ValidationResult result = ValidationResult.createEmpty();
        for (RenderedService renderedService : payments) {
            result = model.getValidator().validate(renderedService, result);
            result = model.getPeriodValidator().validate(renderedService, result);
            if (result.hasErrors()) {
                view.showErrors(result.getErrors());
                return;
            }
        }
        for (RenderedService renderedService : payments) {
            model.save(renderedService);
        }
        view.hide();
    }
}
