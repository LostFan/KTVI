package org.lostfan.ktv.view.entity;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.View;

public class DisconnectionEntityView extends EntityView {

    public DisconnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public DisconnectionEntityView(RenderedServiceEntityModel model, DisconnectionRenderedService entity) {
        super(model, entity);
        setTitle(View.getEntityString(FixedServices.DISCONNECTION.getCode()));

        addFormField(createFormField(model.getDisconnectionReasonField(), entity), model.getDisconnectionReasonField());

        FormView.DateFormField dateField = (FormView.DateFormField) getFormField("renderedService.date");
        FormView.IntegerFormField priceField = (FormView.IntegerFormField) getFormField("renderedService.price");
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
