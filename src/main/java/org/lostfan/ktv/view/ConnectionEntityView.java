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
        setSize();

        addFormField(createFormField(model.getTariffField(), entity), model.getTariffField());

        DateFormField dateField = (DateFormField) getFormField("renderedService.date");
        IntegerFormField priceField = (IntegerFormField) getFormField("renderedService.price");
        dateField.addValueListener(e -> {
            Integer price = model.getRenderedServicePriceByDate(FixedServices.CONNECTION.getId(), dateField.getValue());
            priceField.setValue(price);
        });

        for (FullEntityField fullEntityField : model.getFullFields()) {
            List<Entity> list = new ArrayList<>();
            if(entity != null) {
                list = (List<Entity>) fullEntityField.get(entity);
            }
            this.setInnerTable(new EntityInnerTableView<>(fullEntityField, list));
        }

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new ConnectionRenderedService();
    }
}
