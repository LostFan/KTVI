package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.SubscriptionFeeRecalculationModel;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.validation.ValidationResult;
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
        view.setRecalculateWithActionListener(this::addRecalculateOneActionPerformed);
        view.setRecalculateAllWithActionListener(this::addRecalculateAllActionPerformed);
        view.newDateActionListener(this::newDateActionPerformed);
    }

    protected void addRecalculateAllActionPerformed(Object args) {
        SubscriptionFeeRecalculationModel subscriptionFeeRecalculationModel = new SubscriptionFeeRecalculationModel();
        SubscriptionFeeView entityView = new SubscriptionFeeView(model, subscriptionFeeRecalculationModel, false);
        entityView.setAddActionListener(args_ -> {
            List<RenderedService> renderedServices = (List<RenderedService>) args_;
            model.generateAllSubscriptionFees(renderedServices);
            entityView.hide();
        });
        entityView.setRecalculateActionListener(args_ -> {
            LocalDate date = (LocalDate) args_;
            ValidationResult result = ValidationResult.createEmpty();
            if (date == null) {
                result.addError("errors.empty", "renderedService.date");
            }
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            result = subscriptionFeeRecalculationModel.createSubscriptionFees(date);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
        });

    }

    protected void addRecalculateOneActionPerformed(Object args) {
        SubscriptionFeeRecalculationModel subscriptionFeeRecalculationModel = new SubscriptionFeeRecalculationModel();
        SubscriptionFeeView entityView = new SubscriptionFeeView(this.model, subscriptionFeeRecalculationModel, true);
        entityView.setAddActionListener(args_ -> {
            List<RenderedService> renderedServices = (List<RenderedService>) args_;
            model.generateSeveralSubscriptionFees(renderedServices);
            entityView.hide();
        });
        entityView.setRecalculateActionListener(args_ -> {
            SubscriptionFeeView.DateAndSubscriberId dateAndSubscriberId = (SubscriptionFeeView.DateAndSubscriberId) args_;
            ValidationResult result = ValidationResult.createEmpty();
            if (dateAndSubscriberId.getDate() == null) {
                result.addError("errors.empty", "renderedService.date");
            }
            if (dateAndSubscriberId.getSubscriberId() == null) {
                result.addError("errors.empty", "subscriber");
            }
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            result = subscriptionFeeRecalculationModel.createSubscriptionFeeBySubscriber(dateAndSubscriberId.getSubscriberId(), dateAndSubscriberId.getDate());
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
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
}
