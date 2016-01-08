package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class DisconnectionEntityView extends EntityView {

    public DisconnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public DisconnectionEntityView(RenderedServiceEntityModel model, DisconnectionRenderedService entity) {
        super((EntityModel)model, entity);
        setTitle(getEntityString(FixedServices.DISCONNECTION.getCode()));

        revalidate();
    }

    @Override
    protected Entity createNewEntity() {
        return new DisconnectionRenderedService();
    }
}
