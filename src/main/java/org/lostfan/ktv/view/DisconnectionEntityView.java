package org.lostfan.ktv.view;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class DisconnectionEntityView extends EntityView {

    private Tariff tariff;
    private Map<String, List<MaterialConsumption>> entityInnerTableValues;
    private LabelFieldInput tariffLabelFieldInput;
    private EntityInnerTableView<MaterialConsumption> entityInnerTableView;
    private RenderedServiceEntityModel model;

    public DisconnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public DisconnectionEntityView(RenderedServiceEntityModel model, DisconnectionRenderedService entity) {
        super((EntityModel)model, entity);
        this.model = model;
        setTitle(getEntityString(FixedServices.DISCONNECTION.getCode()));

        revalidate();
    }

    @Override
    protected Entity buildEntity() {
        return model.buildDisconnectionDTO((RenderedService) super.buildEntity());
    }
}
