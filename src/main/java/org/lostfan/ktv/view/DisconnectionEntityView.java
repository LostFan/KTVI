package org.lostfan.ktv.view;

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

        DateFormField dateField = (DateFormField) getFormField("renderedService.date");
        IntegerFormField priceField = (IntegerFormField) getFormField("renderedService.price");
        dateField.addValueListener(e -> {
            Integer price = model.getRenderedServicePriceByDate(FixedServices.DISCONNECTION.getId(), dateField.getValue());
            priceField.setValue(price);
        });
        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new DisconnectionRenderedService();
    }
}
