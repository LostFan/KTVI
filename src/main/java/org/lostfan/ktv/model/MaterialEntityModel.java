package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.MaterialDAO;
import org.lostfan.ktv.domain.Material;

public class MaterialEntityModel extends BaseEntityModel<Material> {

    private List<EntityField<Material, ?>> fields;

    private List<Material> materials;

    public MaterialEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getMaterialDAO();
        fields = new ArrayList<>();


        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("material.id", EntityFieldTypes.Integer, Material::getId, Material::setId));
        this.fields.add(new EntityField<>("material.name", EntityFieldTypes.String, Material::getName, Material::setName));
        this.fields.add(new EntityField<>("material.price", EntityFieldTypes.Integer, Material::getPrice, Material::setPrice));
        this.fields.add(new EntityField<>("material.unit", EntityFieldTypes.String, Material::getUnit, Material::setUnit));
    }

    @Override
    public void saveOrEditEntity(Map<String, Object> collect) {
        Material material = new Material();
        material.setPrice((Integer) collect.get("material.price"));
        material.setName((String) collect.get("material.name"));
        material.setUnit((String) collect.get("material.unit"));
        if(collect.get("material.id") != null) {
            Integer materialId = (Integer) collect.get("material.id");
            if (this.dao.get(materialId) != null) {
                material.setId((Integer) collect.get("material.id"));
                this.dao.update(material);
            } else {
                this.dao.save(material);
            }
        } else {
            this.dao.save(material);
        }
        this.materials = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.materials = this.dao.getAll();
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
        return "material";
    }

    @Override
    public List<EntityField<Material, ?>> getFields() {
        return this.fields;
    }

    public List<Material> getList() {
        if (this.materials == null) {
            this.materials = this.dao.getAll();
        }

        return this.materials;
    }

    @Override
    public List<Material> getListByForeignKey(int foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "materials";
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Material>> criteria) {
        super.setSearchCriteria(criteria);

        this.materials = this.dao.getAll();
        Stream<Material> stream = this.materials.stream();
        for (FieldSearchCriterion<Material> materialFieldSearchCriterion : criteria) {
            stream = stream.filter(materialFieldSearchCriterion.buildPredicate());
        }

        this.materials = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return Material.class;
    }
}
