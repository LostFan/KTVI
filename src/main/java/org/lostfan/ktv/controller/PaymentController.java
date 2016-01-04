package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.PaymentEntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.LoadPaymentsView;
import org.lostfan.ktv.view.PaymentTableView;

import java.util.List;

public class PaymentController extends EntityController{

    private PaymentEntityModel model;
    private PaymentTableView view;

    public PaymentController(PaymentEntityModel model, PaymentTableView view) {
        super(model, view);
        this.model = model;
        this.view = view;
        view.loadActionListener(this::loadActionPerformed);
    }

    private void loadActionPerformed(Object args) {
        LoadPaymentsView loadPaymentsView = new LoadPaymentsView(model);
        loadPaymentsView.setAddActionListener(args_ -> {
            List<Payment> payments = (List<Payment>) args_;
            ValidationResult result = ValidationResult.createEmpty();
            for (Payment payment : payments) {
                result = model.save(payment);
            }
            if (result.hasErrors()) {
                loadPaymentsView.showErrors(result.getErrors());
                return;
            }

            loadPaymentsView.hide();
        });
//        this.loadPaymentsView.setSaveActionListener(this::saveTariffPriceActionPerformed);
    }

}
