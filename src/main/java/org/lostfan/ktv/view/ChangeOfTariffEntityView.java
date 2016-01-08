package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class ChangeOfTariffEntityView extends EntityView {

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model, ChangeOfTariffRenderedService entity) {
        super((EntityModel)model, entity);
        setTitle(getEntityString(FixedServices.CHANGE_OF_TARIFF.getCode()));

        addFormField(createFormField(model.getChangeTariffField(), entity));

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new ChangeOfTariffRenderedService();
    }
}
