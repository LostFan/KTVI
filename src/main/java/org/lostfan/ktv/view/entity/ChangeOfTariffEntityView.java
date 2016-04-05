package org.lostfan.ktv.view.entity;

import java.time.LocalDate;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.View;

public class ChangeOfTariffEntityView extends EntityView {

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model, ChangeOfTariffRenderedService entity) {
        super(model, entity);
        setTitle(View.getEntityString(FixedServices.CHANGE_OF_TARIFF.getCode()));

        FormView.FormField dateField = getFormField("renderedService.date");
        FormView.FormField priceField = getFormField("renderedService.price");
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
