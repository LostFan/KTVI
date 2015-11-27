package org.lostfan.ktv.view;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.DateLabelFormatter;
import org.lostfan.ktv.utils.DefaultContextMenu;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.components.EntityComboBox;
import org.lostfan.ktv.view.components.EntityComboBoxFactory;
import org.lostfan.ktv.view.components.EntityComboBoxModel;
import org.lostfan.ktv.view.components.EntitySelectionFactory;
import org.lostfan.ktv.view.components.EntityViewFactory;

public class RenderedServiceEntityView extends EntityView {

    protected List<LabelFieldInput> tariffLabelFieldInputs;

    public RenderedServiceEntityView(EntityModel model) {
        this(model, null);
    }

    //// TODO: 27-Nov-15 wrong logic
    public <E extends Entity> RenderedServiceEntityView(EntityModel model, Entity entity) {
        super(model, entity);
        RenderedServiceEntityModel renderedServiceEntityModel = (RenderedServiceEntityModel) model;
        for (EntityField entityField : renderedServiceEntityModel.getTariffFields()) {

        }
        LabelFieldInput labelFieldInput = new EntityLabelFieldInput(new EntityField("tariff", EntityFieldTypes.Tariff, Tariff::getId, Tariff::setId), null );
        for (LabelFieldInput innerLabelFieldInput : labelFieldInputs) {
            if(innerLabelFieldInput.getEntityField().getType() == EntityFieldTypes.Service) {
                Service service = ((Service)((EntityComboBox)innerLabelFieldInput.getInputComponent().getComponent(0))
                        .getSelectedEntity());
                if(service == null) {
                    hideTariffField();
                    continue;
                }
                if(service.isConnectionService()) {
                    showTariffField();
                    continue;
                }
                hideTariffField();

            }
        }

        tariffLabelFieldInputs = new ArrayList<>();
        for (EntityField entityField : ((RenderedServiceEntityModel) model).getTariffFields()) {
            if (!entityField.isEditable()) {
                continue;
            }
            tariffLabelFieldInputs.add(createLabelFieldInput(entityField, null));
        }


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < this.tariffLabelFieldInputs.size(); i++) {
            c.gridy = labelFieldInputs.size() + i;
            c.gridx = 0;
            this.contentPanel.add(this.tariffLabelFieldInputs.get(i).label, c);
            c.gridx = 1;
            JComponent inputComponent = this.tariffLabelFieldInputs.get(i).getInputComponent();
            // HACK: textFields is excessively narrow when it's smaller than the displayed area.
            inputComponent.setMinimumSize(inputComponent.getPreferredSize());
            this.contentPanel.add(inputComponent, c);
        }
        this.frame.invalidate();
        this.frame.repaint();
    }

    void  hideTariffField() {

    }

    void showTariffField() {

    }

}
