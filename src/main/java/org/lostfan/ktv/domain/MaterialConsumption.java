package org.lostfan.ktv.domain;

import org.lostfan.ktv.model.SearchCriteria;

/**
 * Created by Roman Savoskin on 21.08.2015.
 */
public class MaterialConsumption extends DefaultEntity {

    private Integer id;

    private Integer materialId;

    private Integer renderedServiceId;

    private Double amount;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        if(id != null) {
            return id.toString();
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterialConsumption that = (MaterialConsumption) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
