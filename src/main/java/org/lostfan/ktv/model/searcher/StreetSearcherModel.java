package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class StreetSearcherModel extends EntitySearcherModel<Street> {

    private List<EntityField> fields;

    public StreetSearcherModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("street.id", EntityFieldTypes.Integer, Street::getId, Street::setId, false));
        this.fields.add(new EntityField("street.name", EntityFieldTypes.String, Street::getName, Street::setName));
    }

    @Override
    public Class getEntityClass() {
        return Street.class;
    }

    @Override
    public String getEntityNameKey() {
        return "streets";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<Street> getDao() {
        return DAOFactory.getDefaultDAOFactory().getStreetDAO();
    }
}
