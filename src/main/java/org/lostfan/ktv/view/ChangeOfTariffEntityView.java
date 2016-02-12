package org.lostfan.ktv.view;

import java.time.LocalDate;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class ChangeOfTariffEntityView extends EntityView {

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model, ChangeOfTariffRenderedService entity) {
        super(model, entity);
        setTitle(getEntityString(FixedServices.CHANGE_OF_TARIFF.getCode()));
        setSize();

        FormField dateField = getFormField("renderedService.date");
        FormField priceField = getFormField("renderedService.price");
        dateField.addValueListener(e ->
                priceField.setValue(model.getRenderedServicePriceByDate(FixedServices.CHANGE_OF_TARIFF.getId(), (LocalDate)dateField.getValue())));

        addFormField(createFormField(model.getTariffField(), entity), model.getTariffField());

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new ChangeOfTariffRenderedService();
    }
}
