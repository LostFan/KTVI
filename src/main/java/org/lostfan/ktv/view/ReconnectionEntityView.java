package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.ReconnectionRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

import java.time.LocalDate;

public class ReconnectionEntityView extends EntityView {

    public ReconnectionEntityView(RenderedServiceEntityModel model) {
        this(model, (ReconnectionRenderedService)null);
    }

    public ReconnectionEntityView(RenderedServiceEntityModel model, ReconnectionRenderedService entity) {
        super(model, entity);
        setTitle(getEntityString(FixedServices.RECONNECTION.getCode()));
        setSize();

        addFormField(createFormField(model.getTariffField(), entity), model.getTariffField());

        FormField tariffField = getFormField("tariff");
        tariffField.getInputComponent().setEnabled(false);
        FormField subscriberField = getFormField("subscriber");

        FormField dateField = getFormField("renderedService.date");
        FormField priceField = getFormField("renderedService.price");
        subscriberField.addValueListener(e -> {
            tariffField.setValue(model.getSubscriberTariff((Integer) subscriberField.getValue(), (LocalDate) dateField.getValue()));
        });
        dateField.addValueListener(e -> {
            priceField.setValue(model.getRenderedServicePriceByDate(FixedServices.RECONNECTION.getId(), (LocalDate) dateField.getValue()));
            tariffField.setValue(model.getSubscriberTariff((Integer) subscriberField.getValue(), (LocalDate) dateField.getValue()));
        });

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new ReconnectionRenderedService();
    }
}
