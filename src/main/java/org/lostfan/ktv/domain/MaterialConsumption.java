package org.lostfan.ktv.domain;

/**
 * Created by Roman Savoskin on 21.08.2015.
 */
public class MaterialConsumption {

    private int id;

    private int materialId;

    private int renderedServiceId;

    private double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
