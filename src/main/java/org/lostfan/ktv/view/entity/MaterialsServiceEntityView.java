package org.lostfan.ktv.view.entity;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.dto.MaterialsRenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.view.EntityInnerTableView;

public class MaterialsServiceEntityView extends EntityView {

    private EntityInnerTableView<MaterialConsumption> entityInnerTableView;

    public MaterialsServiceEntityView(RenderedServiceEntityModel model) {
        this(model, null);
    }

    public MaterialsServiceEntityView(RenderedServiceEntityModel model, MaterialsRenderedService entity) {
        super(model, entity);
        setTitle(getEntityString(FixedServices.MATERIALS.getCode()));

        Service service = new Service();
        if(entity != null) {
            service.setId(entity.getServiceId());
        }

        FormField priceField = getFormField("renderedService.price");
        priceField.getInputComponent().setEnabled(false);
        ((JTextField)priceField.getInputComponent()).setDisabledTextColor(Color.BLACK);

        for (FullEntityField fullEntityField : model.getFullFields()) {
            List<Entity> list = new ArrayList<>();
            if(entity != null) {
                list = (List<Entity>) fullEntityField.get(entity);
            }
            this.entityInnerTableView = new EntityInnerTableView<>(fullEntityField, list);
            this.setInnerTable(this.entityInnerTableView);
        }
        this.entityInnerTableView.getTable().getModel().addTableModelListener(e -> {
            BigDecimal allSum = BigDecimal.ZERO;
            for(int i = 0; i < entityInnerTableView.getTable().getRowCount(); i++ ) {
                Object objPrice = entityInnerTableView.getTable().getValueAt(i, entityInnerTableView.getTable().getColumn(getEntityString("materialConsumption.allPrice")).getModelIndex());
                if(objPrice != null) {
                    allSum = allSum.add((BigDecimal) (objPrice));
                }
            }
            priceField.setValue(allSum.setScale(2, BigDecimal.ROUND_HALF_UP));
        });

        revalidate();
    }

    private Double rounding(Double number) {
        if(number == null) {
            return null;
        }
        Long longNumber = Math.round(number * 100);
        Double newNumber  = longNumber.doubleValue() / 100;
        return newNumber;

    }

    @Override
    protected Entity createNewEntity() {
        return new MaterialsRenderedService();
    }
}
