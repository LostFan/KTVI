package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Material;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.MaterialSearcherModel;
import org.lostfan.ktv.validation.MaterialValidator;
import org.lostfan.ktv.validation.Validator;

public class MaterialEntityModel extends BaseEntityModel<Material> {

    private List<EntityField> fields;

    private Validator<Material> validator = new MaterialValidator();

    public MaterialEntityModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("material.id", EntityFieldTypes.Integer, Material::getId, Material::setId, false));
        this.fields.add(new EntityField("material.name", EntityFieldTypes.String, Material::getName, Material::setName));
        this.fields.add(new EntityField("material.price", EntityFieldTypes.Double, Material::getPrice, Material::setPrice));
        this.fields.add(new EntityField("material.unit", EntityFieldTypes.String, Material::getUnit, Material::setUnit));
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(MainModel.getServiceEntityModel());
        entityModels.add(MainModel.getSubscriberEntityModel());
        return entityModels;
    }

    @Override
    public String getEntityName() {
        return "material";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "materials";
    }

    @Override
    public Class getEntityClass() {
        return Material.class;
    }

    @Override
    protected EntityDAO<Material> getDao() {
        return DAOFactory.getDefaultDAOFactory().getMaterialDAO();
    }

    @Override
    public Material createNewEntity() {
        return new Material();
    }

    @Override
    public Validator<Material> getValidator() {
        return validator;
    }

    @Override
    public EntitySearcherModel<Material> createSearchModel() {
        return new MaterialSearcherModel();
    }
}
