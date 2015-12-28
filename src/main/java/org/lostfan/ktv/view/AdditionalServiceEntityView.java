package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

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
    private EntityInnerTableView entityInnerTableView;
    private Service service;

    public AdditionalServiceEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public AdditionalServiceEntityView(RenderedServiceEntityModel model, AdditionalRenderedService entity) {
        super((EntityModel)model, entity);
        setTitle(getEntityString(FixedServices.ADDITIONAL_SERVICE.getCode()));

        Service service = new Service();
        if(entity != null) {
            service.setId(entity.getServiceId());
        }
        serviceLabelFieldInput = new EntityLabelFieldInput(model.getServiceField(), service, EntityComboBoxFactory.createAdditionalServiceComboBox(), EntitySelectionFactory::createAdditionalServiceForm);
        for (ActionListener actionListener : this.addButton.getActionListeners()) {
            this.addButton.removeActionListener(actionListener);
        }
        this.addButton.addActionListener(e -> {
            if (this.addActionListener != null) {
                entityInnerTableView.stopEditing();
                this.addActionListener.actionPerformed(model.buildAdditionalServiceDTO((RenderedService) getEntity(), getService(), entityInnerTableValues));
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = labelFieldInputs.size() + 1;
        c.gridx = 0;
        getFieldPanel().add(this.serviceLabelFieldInput.label, c);
        c.gridx = 1;
        JComponent inputComponent = this.serviceLabelFieldInput.getInputComponent();
        // HACK: textFields is excessively narrow when it's smaller than the displayed area.
        inputComponent.setMinimumSize(inputComponent.getPreferredSize());
        getFieldPanel().add(inputComponent, c);

        this.entityInnerTableValues = new HashMap<>();
        for (FullEntityField fullEntityField : model.getFullFields()) {
            List<Entity> list = new ArrayList<>();
            if(entity != null) {
                list = (List<Entity>) fullEntityField.get(entity);
            }
            this.entityInnerTableView = new EntityInnerTableView(fullEntityField, list);
            this.entityInnerTableValues.put(fullEntityField.getTitleKey(), this.entityInnerTableView.getEntityList());
            this.addInnerTable(this.entityInnerTableView);
        }

        revalidate();
    }

    public Service getService() {
        Service entity = this.service;
        if (entity == null) {
            entity = new Service();
        }
        this.serviceLabelFieldInput.entityField.set(entity, this.serviceLabelFieldInput.getValue());
        return entity;
    }
}
