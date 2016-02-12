package org.lostfan.ktv.view;

import java.time.LocalDate;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class DisconnectionEntityView extends EntityView {

    public DisconnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public DisconnectionEntityView(RenderedServiceEntityModel model, DisconnectionRenderedService entity) {
        super(model, entity);
        setTitle(getEntityString(FixedServices.DISCONNECTION.getCode()));
        setSize();

        FormField dateField = getFormField("renderedService.date");
        FormField priceField = getFormField("renderedService.price");
        dateField.addValueListener(e ->
                priceField.setValue(model.getRenderedServicePriceByDate(FixedServices.DISCONNECTION.getId(), (LocalDate)dateField.getValue())));

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new DisconnectionRenderedService();
    }
}
