package org.lostfan.ktv.view.entity;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.View;

public class ConnectionEntityView extends EntityView {

    public ConnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ConnectionEntityView(RenderedServiceEntityModel model, ConnectionRenderedService entity) {
        super(model, entity);
        setTitle(View.getEntityString(FixedServices.CONNECTION.getCode()));

        addFormField(createFormField(model.getTariffField(), entity), model.getTariffField());

        FormView.DateFormField dateField = (FormView.DateFormField) getFormField("renderedService.date");
        FormView.IntegerFormField priceField = (FormView.IntegerFormField) getFormField("renderedService.price");
        dateField.addValueListener(e -> {
            Integer price = model.getRenderedServicePriceByDate(FixedServices.CONNECTION.getId(), dateField.getValue());
            priceField.setValue(price);
        });

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new ConnectionRenderedService();
    }
}
