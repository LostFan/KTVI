package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.entity.PaymentEntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.LoadPaymentsView;
import org.lostfan.ktv.view.PaymentTableView;

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

}
