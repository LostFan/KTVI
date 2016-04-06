package org.lostfan.ktv.controller;

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
        view.setCountWithActionListener(this::addCountWithActionPerformed);
        view.setCountAllWithActionListener(this::addCountAllWithActionPerformed);
        view.newDateActionListener(this::newDateActionPerformed);
    }

    protected void addCountAllWithActionPerformed(Object args) {
        SubscriptionFeeView entityView = new SubscriptionFeeView(this.model, false);
        entityView.setAddActionListener(args_ -> {
            LocalDate date = (LocalDate) args_;
            ValidationResult result = ValidationResult.createEmpty();
            if(date == null) {
                result.addError("errors.empty", "renderedService.date");
            }
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            result = model.createSubscriptionFees(date);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    protected void addCountWithActionPerformed(Object args) {
        SubscriptionFeeView entityView = new SubscriptionFeeView(this.model, true);
        entityView.setAddActionListener(args_ -> {
            SubscriptionFeeView.DateAndSubscriberId dateAndSubscriberId = (SubscriptionFeeView.DateAndSubscriberId) args_;
            ValidationResult result = ValidationResult.createEmpty();
            if(dateAndSubscriberId.getDate() == null) {
                result.addError("errors.empty", "renderedService.date");
            }
            if(dateAndSubscriberId.getSubscriberId() == null) {
                result.addError("errors.empty", "subscriber");
            }
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            result = model.createSubscriptionFeeBySubscriber(dateAndSubscriberId.getSubscriberId(), dateAndSubscriberId.getDate());
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

    @Override
    protected void deleteActionPerformed(Object args) {
        List<Integer> selectedIds = (List<Integer>) args;
        ValidationResult result = model.deleteEntityById(selectedIds);
        if (result.hasErrors()) {
            view.errorWindow(result.getErrors());
        }
    }
}
