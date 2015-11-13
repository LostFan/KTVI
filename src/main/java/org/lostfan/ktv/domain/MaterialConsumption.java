package org.lostfan.ktv.domain;

import org.lostfan.ktv.model.SearchCriteria;

/**
 * Created by Roman Savoskin on 21.08.2015.
 */
public class MaterialConsumption implements Entity {

    private Integer id;

    private Integer materialId;

    private Integer renderedServiceId;

    private double amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getRenderedServiceId() {
        return renderedServiceId;
    }

    public void setRenderedServiceId(Integer renderedServiceId) {
        this.renderedServiceId = renderedServiceId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return id.toString();
    }
}
