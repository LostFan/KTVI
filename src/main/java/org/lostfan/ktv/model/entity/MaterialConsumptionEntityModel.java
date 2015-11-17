package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.MaterialConsumptionDAO;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.validation.MaterialConsumptionValidator;
import org.lostfan.ktv.validation.Validator;

public class MaterialConsumptionEntityModel extends BaseEntityModel<MaterialConsumption> {

    private List<EntityField> fields;

    private Validator<MaterialConsumption> validator = new MaterialConsumptionValidator();

    public MaterialConsumptionEntityModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("materialConsumption.id", EntityFieldTypes.Integer, MaterialConsumption::getId, MaterialConsumption::setId, false));
        this.fields.add(new EntityField("material", EntityFieldTypes.Material, MaterialConsumption::getMaterialId, MaterialConsumption::setMaterialId));
        this.fields.add(new EntityField("renderedService", EntityFieldTypes.RenderedService, MaterialConsumption::getRenderedServiceId, MaterialConsumption::setRenderedServiceId));
        this.fields.add(new EntityField("materialConsumption.amount", EntityFieldTypes.Double, MaterialConsumption::getAmount, MaterialConsumption::setAmount));
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

    @Override
    public List<MaterialConsumption> getListByForeignKey(Integer foreignKey) {
        if(foreignKey == null) {
            entities = new ArrayList<>();
            return entities;
        }
        if (this.getParentModel().getEntityClass() == RenderedService.class) {
            entities = getDao().getMaterialConsumptionsByRenderedServiceId(foreignKey);
        }
        return entities;
    }

    @Override
    public String getEntityNameKey() {
        return "materialConsumptions";
    }

    @Override
    public Class getEntityClass() {
        return MaterialConsumption.class;
    }

    @Override
    protected MaterialConsumptionDAO getDao() {
        return DAOFactory.getDefaultDAOFactory().getMaterialConsumptionDAO();
    }

    @Override
    public Validator<MaterialConsumption> getValidator() {
        return this.validator;
    }

    @Override
    public MaterialConsumption createNewEntity() {
        return new MaterialConsumption();
    }
}
