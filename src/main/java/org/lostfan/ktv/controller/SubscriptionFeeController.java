package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.EntityFilterView;
import org.lostfan.ktv.view.SubscriptionFeeTableView;
import org.lostfan.ktv.view.SubscriptionFeeView;

import java.time.LocalDate;

public class SubscriptionFeeController extends EntityController{

    private SubscriptionFeeModel model;
    private SubscriptionFeeTableView view;
    private EntityFilterView entityFilterView;

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
                result.addError("empty", "renderedService.date");
            }
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            model.createSubscriptionFees(date);
            entityView.hide();
        });
    }

    protected void addCountWithActionPerformed(Object args) {
        SubscriptionFeeView entityView = new SubscriptionFeeView(this.model, true);
        entityView.setAddActionListener(args_ -> {
            SubscriptionFeeView.DateAndSubscriberId dateAndSubscriberId = (SubscriptionFeeView.DateAndSubscriberId) args_;
            ValidationResult result = ValidationResult.createEmpty();
            if(dateAndSubscriberId.getDate() == null) {
                result.addError("empty", "renderedService.date");
            }
            if(dateAndSubscriberId.getSubscriberId() == null) {
                result.addError("empty", "subscriber");
            }

            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            model.createSubscriptionFeeBySubscriber(dateAndSubscriberId.getSubscriberId(), dateAndSubscriberId.getDate());
            entityView.hide();
        });
    }

    private void newDateActionPerformed(Object args) {
        LocalDate date = (LocalDate) args;
        this.model.setDate(date);
    }
}
