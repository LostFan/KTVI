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
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.ResourceBundles;

public class DisconnectionEntityView extends EntityView{

    private Tariff tariff;
    private Map<String, List<MaterialConsumption>> entityInnerTableValues;
    private LabelFieldInput tariffLabelFieldInput;
    private EntityInnerTableView entityInnerTableView;

    public DisconnectionEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public DisconnectionEntityView(RenderedServiceEntityModel model, DisconnectionRenderedService entity) {
        super((EntityModel)model, entity);
        this.frame.setTitle(ResourceBundles.getEntityBundle().getString(FixedServices.DISCONNECTION.getCode()));

        for (ActionListener actionListener : this.addButton.getActionListeners()) {
            this.addButton.removeActionListener(actionListener);
        }
        this.addButton.addActionListener(e -> {
            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(model.buildDisconnectionDTO((RenderedService) getEntity()));
            }
        });


        this.frame.invalidate();
        this.frame.repaint();
    }
}
