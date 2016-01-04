package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class ChangeOfTariffEntityView extends EntityView {

    private Tariff tariff;
    private LabelFieldInput tariffLabelFieldInput;
    private RenderedServiceEntityModel model;

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model, ChangeOfTariffRenderedService entity) {
        super((EntityModel)model, entity);
        this.model = model;
        setTitle(getEntityString(FixedServices.CHANGE_OF_TARIFF.getCode()));

        Tariff tariff = new Tariff();
        if(entity != null) {
            tariff.setId(entity.getTariffId());
        }
        this.tariffLabelFieldInput = createLabelFieldInput(model.getTariffField(), tariff);
        addLabelFieldInput(this.tariffLabelFieldInput);

        revalidate();
    }

    @Override
    protected Entity buildEntity() {
        return model.buildChangeOfTariffDTO((RenderedService) super.buildEntity(), getTariff());
    }

    public Tariff getTariff() {
        Tariff entity = this.tariff;
        if (entity == null) {
            entity = new Tariff();
        }
        this.tariffLabelFieldInput.entityField.set(entity, this.tariffLabelFieldInput.getValue());
        return entity;
    }
}
