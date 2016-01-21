package org.lostfan.ktv.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class ConnectionEntityView extends EntityView {

    private EntityInnerTableView<MaterialConsumption> entityInnerTableView;

    public ConnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ConnectionEntityView(RenderedServiceEntityModel model, ConnectionRenderedService entity) {
        super(model, entity);
        setTitle(getEntityString(FixedServices.CONNECTION.getCode()));

        addFormField(createFormField(model.getConnectionTariffField(), entity), model.getConnectionTariffField());

        FormField dateField = getFormField("renderedService.date");
        FormField priceField = getFormField("renderedService.price");
        dateField.addValueListener(e ->
                priceField.setValue(model.getRenderedServicePriceByDate(FixedServices.CONNECTION.getId(), (LocalDate)dateField.getValue())));

        for (FullEntityField fullEntityField : model.getFullFields()) {
            List<Entity> list = new ArrayList<>();
            if(entity != null) {
                list = (List<Entity>) fullEntityField.get(entity);
            }
            this.entityInnerTableView = new EntityInnerTableView<>(fullEntityField, list);
            this.setInnerTable(this.entityInnerTableView);
        }

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new ConnectionRenderedService();
    }
}
