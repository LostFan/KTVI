package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Equipment;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.EquipmentSearcherModel;
import org.lostfan.ktv.validation.EquipmentValidator;
import org.lostfan.ktv.validation.Validator;

public class EquipmentEntityModel extends BaseEntityModel<Equipment> {

    private List<EntityField> fields;

    private Validator<Equipment> validator = new EquipmentValidator();

    public EquipmentEntityModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("equipment.id", EntityFieldTypes.Integer, Equipment::getId, Equipment::setId, false));
        this.fields.add(new EntityField("equipment.name", EntityFieldTypes.String, Equipment::getName, Equipment::setName));
        this.fields.add(new EntityField("equipment.price", EntityFieldTypes.Double, Equipment::getPrice, Equipment::setPrice));
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
        return "equipment";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "equipments";
    }

    @Override
    public Class getEntityClass() {
        return Equipment.class;
    }

    @Override
    protected EntityDAO<Equipment> getDao() {
        return DAOFactory.getDefaultDAOFactory().getEquipmentDAO();
    }

    @Override
    public Equipment createNewEntity() {
        return new Equipment();
    }

    @Override
    public Validator<Equipment> getValidator() {
        return validator;
    }

    @Override
    public EntitySearcherModel<Equipment> createSearchModel() {
        return new EquipmentSearcherModel();
    }
}
