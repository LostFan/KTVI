package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.ResourceBundles;

public class ChangeOfTariffEntityView extends EntityView{

    private Tariff tariff;
    private LabelFieldInput tariffLabelFieldInput;

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public ChangeOfTariffEntityView(RenderedServiceEntityModel model, ChangeOfTariffRenderedService entity) {
        super((EntityModel)model, entity);
        this.frame.setTitle(ResourceBundles.getEntityBundle().getString(FixedServices.CHANGE_OF_TARIFF.getCode()));

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
                this.addActionListener.actionPerformed(model.buildChangeOfTariffDTO((RenderedService) getEntity(), getTariff()));
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = labelFieldInputs.size() + 1;
        c.gridx = 0;
        this.contentPanel.add(this.tariffLabelFieldInput.label, c);
        c.gridx = 1;
        JComponent inputComponent = this.tariffLabelFieldInput.getInputComponent();
        // HACK: textFields is excessively narrow when it's smaller than the displayed area.
        inputComponent.setMinimumSize(inputComponent.getPreferredSize());
        this.contentPanel.add(inputComponent, c);

        this.frame.invalidate();
        this.frame.repaint();
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
