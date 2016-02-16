package org.lostfan.ktv.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.dto.AdditionalRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.components.EntityPanelFactory;

public class AdditionalServiceEntityView extends EntityView {

    private EntityInnerTableView<MaterialConsumption> entityInnerTableView;

    public AdditionalServiceEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public AdditionalServiceEntityView(RenderedServiceEntityModel model, AdditionalRenderedService entity) {
        super(model, entity);
        setTitle(getEntityString(FixedServices.ADDITIONAL_SERVICE.getCode()));

        Service service = new Service();
        if(entity != null) {
            service.setId(entity.getServiceId());
        }
        addFormField(new EntityFormField(model.getServiceField(), entity, EntityPanelFactory.createAdditionalServiceEntityPanel())
                , model.getServiceField());

        FormField dateField = getFormField("renderedService.date");
        FormField priceField = getFormField("renderedService.price");
        FormField serviceField = getFormField("service");
        dateField.addValueListener(e ->
                priceField.setValue(model.getRenderedServicePriceByDate((Integer) serviceField.getValue(), (LocalDate)dateField.getValue())));
        serviceField.addValueListener(e -> {
            priceField.setValue(model.getRenderedServicePriceByDate((Integer) serviceField.getValue(), (LocalDate)dateField.getValue()));
        });


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
        return new AdditionalRenderedService();
    }
}
