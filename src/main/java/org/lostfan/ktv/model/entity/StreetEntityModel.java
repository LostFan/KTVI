package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

import java.util.ArrayList;
import java.util.List;

public class StreetEntityModel extends BaseEntityModel<Street> {

    private List<EntityField> fields;

    public StreetEntityModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("street.id", EntityFieldTypes.Integer, Street::getId, Street::setId, false));
        this.fields.add(new EntityField("street.name", EntityFieldTypes.String, Street::getName, Street::setName));
    }


    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public String getEntityName() {
        return "street";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "streets";
    }

    @Override
    public Class getEntityClass() {
        return Street.class;
    }

    @Override
    protected EntityDAO<Street> getDao() {
        return DAOFactory.getDefaultDAOFactory().getStreetDAO();
    }

    @Override
    public Street createNewEntity() {
        return new Street();
    }
}
