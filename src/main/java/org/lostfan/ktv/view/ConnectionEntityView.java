package org.lostfan.ktv.view;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class ConnectionEntityView extends EntityView {

    public ConnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ConnectionEntityView(RenderedServiceEntityModel model, ConnectionRenderedService entity) {
        super(model, entity);
        setTitle(getEntityString(FixedServices.CONNECTION.getCode()));

        addFormField(createFormField(model.getTariffField(), entity), model.getTariffField());

        DateFormField dateField = (DateFormField) getFormField("renderedService.date");
        IntegerFormField priceField = (IntegerFormField) getFormField("renderedService.price");
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
