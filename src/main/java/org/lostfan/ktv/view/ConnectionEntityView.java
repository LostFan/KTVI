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
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;

public class ConnectionEntityView extends EntityView {

    private Tariff tariff;
    private Map<String, List<MaterialConsumption>> entityInnerTableValues;
    private LabelFieldInput tariffLabelFieldInput;
    private EntityInnerTableView entityInnerTableView;

    public ConnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ConnectionEntityView(RenderedServiceEntityModel model, ConnectionRenderedService entity) {
        super((EntityModel)model, entity);
        setTitle(getEntityString(FixedServices.CONNECTION.getCode()));

        Tariff tariff = new Tariff();
        if(entity != null) {
            tariff.setId(entity.getTariffId());
        }
        tariffLabelFieldInput = createLabelFieldInput(model.getTariffField(), tariff);
        for (ActionListener actionListener : this.addButton.getActionListeners()) {
            this.addButton.removeActionListener(actionListener);
        }
        this.addButton.addActionListener(e -> {
            if (this.addActionListener != null) {
                entityInnerTableView.stopEditing();
                this.addActionListener.actionPerformed(model.buildConnectionDTO((RenderedService) getEntity(), getTariff(), entityInnerTableValues));
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = labelFieldInputs.size() + 1;
        c.gridx = 0;
        getFieldPanel().add(this.tariffLabelFieldInput.label, c);
        c.gridx = 1;
        JComponent inputComponent = this.tariffLabelFieldInput.getInputComponent();
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

    public Tariff getTariff() {
        Tariff entity = this.tariff;
        if (entity == null) {
            entity = new Tariff();
        }
        this.tariffLabelFieldInput.entityField.set(entity, this.tariffLabelFieldInput.getValue());
        return entity;
    }
}
