package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.SubscriptionFeeTableView;
import org.lostfan.ktv.view.SubscriptionFeeView;
import org.lostfan.ktv.view.View;

import java.time.LocalDate;
import java.util.List;

public class SubscriptionFeeController implements MainInnerController{

    private SubscriptionFeeModel model;
    private SubscriptionFeeTableView view;
    private EntitySearchView entitySearchView;

    public SubscriptionFeeController(SubscriptionFeeModel model, SubscriptionFeeTableView view) {
        this.model = model;
        this.view = view;
        view.setFindActionListener(this::findActionPerformed);
        view.setCountWithActionListener(this::addCountWithActionPerformed);
        view.setCountAllWithActionListener(this::addCountAllWithActionPerformed);
        view.newDateActionListener(this::newDateActionPerformed);
    }

    @Override
    public View getView() {
        return view;
    }

    protected void findActionPerformed(Object args) {
        if (this.entitySearchView == null) {
            this.entitySearchView = new EntitySearchView(model);
            this.entitySearchView.setFindActionListener(this::searchFindActionPerformed);
        }
        this.entitySearchView.show();
    }

    protected void searchFindActionPerformed(Object args) {
        List<FieldSearchCriterion> criteria = this.entitySearchView.getSearchCriteria();
        this.model.setSearchCriteria(criteria);
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
