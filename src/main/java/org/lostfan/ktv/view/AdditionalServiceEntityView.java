package org.lostfan.ktv.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.dto.AdditionalRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.components.EntityComboBoxFactory;
import org.lostfan.ktv.view.components.EntitySelectionFactory;

public class AdditionalServiceEntityView extends EntityView {

    private Map<String, List<MaterialConsumption>> entityInnerTableValues;
    private LabelFieldInput serviceLabelFieldInput;
    private EntityInnerTableView<MaterialConsumption> entityInnerTableView;
    private RenderedServiceEntityModel model;
    private Service service;

    public AdditionalServiceEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public AdditionalServiceEntityView(RenderedServiceEntityModel model, AdditionalRenderedService entity) {
        super((EntityModel)model, entity);
        this.model = model;
        setTitle(getEntityString(FixedServices.ADDITIONAL_SERVICE.getCode()));

        Service service = new Service();
        if(entity != null) {
            service.setId(entity.getServiceId());
        }
        this.serviceLabelFieldInput = new EntityLabelFieldInput(model.getServiceField(), service, EntityComboBoxFactory.createAdditionalServiceComboBox(), EntitySelectionFactory::createAdditionalServiceForm);
        addLabelFieldInput(this.serviceLabelFieldInput);

        this.entityInnerTableValues = new HashMap<>();
        for (FullEntityField fullEntityField : model.getFullFields()) {
            List<Entity> list = new ArrayList<>();
            if(entity != null) {
                list = (List<Entity>) fullEntityField.get(entity);
            }
            this.entityInnerTableView = new EntityInnerTableView<>(fullEntityField, list);
            this.entityInnerTableValues.put(fullEntityField.getTitleKey(), this.entityInnerTableView.getEntityList());
            this.setInnerTable(this.entityInnerTableView);
        }

        revalidate();
    }

    @Override
    protected Entity buildEntity() {
        return model.buildAdditionalServiceDTO((RenderedService) super.buildEntity(), getService(), entityInnerTableValues);
    }

    private Service getService() {
        Service entity = this.service;
        if (entity == null) {
            entity = new Service();
        }
        this.serviceLabelFieldInput.entityField.set(entity, this.serviceLabelFieldInput.getValue());
        return entity;
    }
}
