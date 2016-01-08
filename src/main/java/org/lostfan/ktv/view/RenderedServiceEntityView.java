package org.lostfan.ktv.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.components.EntityComboBox;

public class RenderedServiceEntityView extends EntityView {

    protected List<FormField> tariffFormFields;

    public RenderedServiceEntityView(EntityModel model) {
        this(model, null);
    }

    //// TODO: 27-Nov-15 wrong logic
    public <E extends Entity> RenderedServiceEntityView(EntityModel model, Entity entity) {
        super(model, entity);
        RenderedServiceEntityModel renderedServiceEntityModel = (RenderedServiceEntityModel) model;
        FormField labelFieldInput = new EntityFormField(new EntityField("tariff", EntityFieldTypes.Tariff, Tariff::getId, Tariff::setId), null );

//        tariffFormFields = new ArrayList<>();
//        for (EntityField entityField : ((RenderedServiceEntityModel) model).getTariffFields()) {
//            if (!entityField.isEditable()) {
//                continue;
//            }
//            tariffFormFields.add(createFormField(entityField, null));
//        }

//        for (int i = 0; i < this.tariffFormFields.size(); i++) {
//            addLabelFieldInput(this.tariffFormFields.get(i));
//        }
        revalidate();
    }

    @Override
    protected void addFormField(FormField field) {
        super.addFormField(field);
//        if(field.getEntityField().getType() == EntityFieldTypes.Service) {
//            Service service = ((Service)((EntityComboBox)field.getInputComponent().getComponent(0))
//                    .getSelectedEntity());
//            if(service == null) {
//                hideTariffField();
//            }
//            if(service.isConnectionService()) {
//                showTariffField();
//            }
//            hideTariffField();
//        }
    }

}
