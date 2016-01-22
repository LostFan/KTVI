package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.model.entity.ServiceEntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.ServicePriceView;
import org.lostfan.ktv.view.ServiceTableView;

public class ServiceEntityController extends EntityController {

    private ServiceEntityModel model;
    private ServicePriceView servicePriceView;

    public ServiceEntityController(ServiceEntityModel model, ServiceTableView view) {
        super(model, view);
        this.model = model;
        view.setPriceActionListener(this::priceActionPerformed);
    }

    private void priceActionPerformed(Object args) {
        Integer id = (Integer) args;
        this.servicePriceView = new ServicePriceView(model.getServiceWithPrices(id));
        this.servicePriceView.setSaveActionListener(this::saveServicePriceActionPerformed);
        this.servicePriceView.setDeleteActionListener(this::deleteServicePriceActionPerformed);
    }

    private void saveServicePriceActionPerformed(Object args) {
        ServicePrice servicePrice = (ServicePrice) args;
        ValidationResult result = this.model.save(servicePrice);
        if (result.hasErrors()) {
            this.servicePriceView.showErrors(result);
        } else {
            this.servicePriceView.hide();
        }
    }

    private void deleteServicePriceActionPerformed(Object args) {
        ServicePrice servicePrice = (ServicePrice) args;
        this.model.delete(servicePrice);
        this.servicePriceView.hide();

    }
}
