package org.lostfan.ktv.view;

import java.time.LocalDate;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.components.EntityPanelFactory;

public class AdditionalServiceEntityView extends EntityView {

    private EntityInnerTableView<MaterialConsumption> entityInnerTableView;

    public AdditionalServiceEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public AdditionalServiceEntityView(RenderedServiceEntityModel model, RenderedService entity) {
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

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new RenderedService();
    }
}
