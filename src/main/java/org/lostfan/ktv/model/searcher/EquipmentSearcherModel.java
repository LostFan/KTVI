package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Equipment;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class EquipmentSearcherModel extends EntitySearcherModel<Equipment> {

    private List<EntityField> fields;

    public EquipmentSearcherModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("equipment.id", EntityFieldTypes.Integer, Equipment::getId, Equipment::setId, false));
        this.fields.add(new EntityField("equipment.name", EntityFieldTypes.String, Equipment::getName, Equipment::setName));
        this.fields.add(new EntityField("equipment.price", EntityFieldTypes.Integer, Equipment::getPrice, Equipment::setPrice));
    }

    @Override
    public Class getEntityClass() {
        return Equipment.class;
    }

    @Override
    public String getEntityNameKey() {
        return "equipments";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<Equipment> getDao() {
        return DAOFactory.getDefaultDAOFactory().getEquipmentDAO();
    }
}
