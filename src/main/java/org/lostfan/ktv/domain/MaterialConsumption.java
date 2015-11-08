package org.lostfan.ktv.domain;

import org.lostfan.ktv.model.SearchCriteria;

/**
 * Created by Roman Savoskin on 21.08.2015.
 */
public class MaterialConsumption implements Entity {

    private Integer id;

    private int materialId;

    private int renderedServiceId;

    private double amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getRenderedServiceId() {
        return renderedServiceId;
    }

    public void setRenderedServiceId(int renderedServiceId) {
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
