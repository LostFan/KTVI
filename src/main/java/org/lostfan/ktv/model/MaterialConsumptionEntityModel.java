package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.MaterialConsumptionDAO;
import org.lostfan.ktv.domain.MaterialConsumption;

public class MaterialConsumptionEntityModel extends BaseEntityModel<MaterialConsumption> {

    private List<EntityField> fields;

    private List<MaterialConsumption> materialConsumptions;

    public MaterialConsumptionEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getMaterialConsumptionDAO();
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("materialConsumption.id", EntityFieldTypes.Integer, MaterialConsumption::getId, MaterialConsumption::setId));
        this.fields.add(new EntityField("material", EntityFieldTypes.Material, MaterialConsumption::getMaterialId, MaterialConsumption::setMaterialId));
        this.fields.add(new EntityField("renderedService", EntityFieldTypes.RenderedService, MaterialConsumption::getRenderedServiceId, MaterialConsumption::setRenderedServiceId));
        this.fields.add(new EntityField("materialConsumption.amount", EntityFieldTypes.Double, MaterialConsumption::getAmount, MaterialConsumption::setAmount));
    }

    @Override
    public void saveOrEditEntity(Map<String, Object> collect) {
        MaterialConsumption materialConsumption = new MaterialConsumption();
        materialConsumption.setMaterialId((Integer) collect.get("material"));
        materialConsumption.setRenderedServiceId((Integer) collect.get("service"));
        materialConsumption.setAmount((Double) collect.get("materialConsumption.amount"));
        if(collect.get("materialConsumption.id") != null) {
            Integer materialConsumptionId = (Integer) collect.get("materialConsumption.id");
            if (this.dao.get(materialConsumptionId) != null) {
                materialConsumption.setId((Integer) collect.get("materialConsumption.id"));
                this.dao.update(materialConsumption);
            } else {
                this.dao.save(materialConsumption);
            }
        } else {
            this.dao.save(materialConsumption);
        }
        this.materialConsumptions = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.materialConsumptions = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(new ServiceEntityModel());
        entityModels.add(new SubscriberEntityModel());
        return entityModels;
    }

    @Override
    public String getEntityName() {
        return "materialConsumption";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    public List<MaterialConsumption> getList() {
        if (this.materialConsumptions == null) {
            this.materialConsumptions = this.dao.getAll();
        }

        return this.materialConsumptions;
    }

    @Override
    public List<MaterialConsumption> getListByForeignKey(int foreignKey) {
        if (this.materialConsumptions == null) {
            this.materialConsumptions = ((MaterialConsumptionDAO)this.dao).getMaterialConsumptionsByRenderedServiceId(foreignKey);
        }

        return this.materialConsumptions;
    }

    @Override
    public String getEntityNameKey() {
        return "materialConsumptions";
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<MaterialConsumption>> criteria) {
        super.setSearchCriteria(criteria);

        this.materialConsumptions = this.dao.getAll();
        Stream<MaterialConsumption> stream = this.materialConsumptions.stream();
        for (FieldSearchCriterion<MaterialConsumption> materialConsumptionFieldSearchCriterion : criteria) {
            stream = stream.filter(materialConsumptionFieldSearchCriterion.buildPredicate());
        }

        this.materialConsumptions = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return MaterialConsumption.class;
    }
}
