package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.entity.PaymentEntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.LoadPaymentsView;
import org.lostfan.ktv.view.PaymentTableView;
import org.lostfan.ktv.view.PaymentView;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class PaymentController extends EntityController{

    public static class FileAndPayments {

        private File file;

        private List<Payment> payments;

        public FileAndPayments(File file, List<Payment> payments) {
            this.file = file;
            this.payments = payments;
        }

        public File getFile() {
            return file;
        }

        public List<Payment> getPayments() {
            return payments;
        }
    }

    private PaymentEntityModel model;
    private PaymentTableView view;

    public PaymentController(PaymentEntityModel model, PaymentTableView view) {
        super(model, view);
        this.model = model;
        this.view = view;
        view.loadActionListener(this::loadActionPerformed);
        view.newDateActionListener(this::newDateActionPerformed);
    }

    private void loadActionPerformed(Object args) {
        LoadPaymentsView loadPaymentsView = new LoadPaymentsView(model);
        loadPaymentsView.setAddActionListener(args_ -> {
            List<Payment> payments = (List<Payment>) args_;
            savePayments(payments, loadPaymentsView);
        });

        loadPaymentsView.setLoadPaymentFileListener(args_ -> {
            FileAndPayments fileAndPayments = (FileAndPayments) args_;
            loadPaymentsView.addPayments(
                    model.createPayments(fileAndPayments.getFile(), fileAndPayments.getPayments()));
        });
//        this.loadPaymentsView.setSaveActionListener(this::saveTariffPriceActionPerformed);
    }

    private void newDateActionPerformed(Object args) {
        LocalDate date = (LocalDate) args;
        this.model.setDate(date);
    }

    @Override
    public void addActionPerformed(Object args) {
        PaymentView entityView = new PaymentView(model);
        entityView.setAddPaymentsInTableListener(args1 -> {
            Payment payment = (Payment) args1;
            entityView.addPayments(model.loadPayments(payment, null));
        });
        entityView.setAddActionListener(args_ -> {
            List<Payment> payments = (List<Payment>) args_;
            savePayments(payments, entityView);
        });
    }

    private void savePayments(List<Payment> payments, FormView view) {
        ValidationResult result = ValidationResult.createEmpty();
        for (Payment payment : payments) {
            result = model.getValidator().validate(payment, result);
            result = model.getPeriodValidator().validate(payment, result);
            if (result.hasErrors()) {
                view.showErrors(result.getErrors());
                return;
            }
        }

        for (Payment payment : payments) {
            model.save(payment);
        }
        view.hide();
    }

    protected void changeActionPerformed(Object args) {
        int selectedId = (Integer) args;

        Payment entity = model.getEntity(selectedId);
        PaymentView entityView = new PaymentView(model, entity);

        entityView.setAddPaymentsInTableListener(args1 -> {
            Payment payment = (Payment) args1;
            entityView.addPayments(model.loadPayments(payment, entity));
        });
        entityView.setAddActionListener(args_ -> {
            List<Payment> payments = (List<Payment>) args_;
            updatePayments(payments,
                    model.getList(entity.getSubscriberAccount(),entity.getDate(),entity.getBankFileName()),
                    entityView);
        });
    }

    private void updatePayments(List<Payment> payments, List<Payment> oldPayments, FormView view) {
        ValidationResult result = ValidationResult.createEmpty();

        for (Payment payment : oldPayments) {
            result = model.getValidator().validate(payment, result);
            result = model.getPeriodValidator().validate(payment, result);
            if (result.hasErrors()) {
                view.showErrors(result.getErrors());
                return;
            }

        }

        for (Payment payment : payments) {
            result = model.getValidator().validate(payment, result);
            result = model.getPeriodValidator().validate(payment, result);
            if (result.hasErrors()) {
                view.showErrors(result.getErrors());
                return;
            }
        }

        model.update(payments, oldPayments);

        view.hide();
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
