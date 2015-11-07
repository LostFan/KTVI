package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.StreetDAO;
import org.lostfan.ktv.domain.Street;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class StreetComboBoxModel extends EntityComboBoxModel<Street> {

    private StreetDAO dao;
    private List<Street> streets;
    private List<EntityField<Street, ?>> fields;
    private EntityField entityFieldId;
    private EntityField entityFieldName;


    public StreetComboBoxModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getStreetDAO();

        this.fields = new ArrayList<>();
        entityFieldId = new EntityField<>("street.id", Types.Integer, Street::getId, Street::setId);
        entityFieldName = new EntityField<>("street.name", Types.String, Street::getName, Street::setName);
    }


    @Override
    public void setListByBeginningPartOfName(String str) {
        this.streets = dao.getStreetsByBeginningPartOfName(str);
        this.notifyObservers(null);
    }

    @Override
    public List<Street> getList() {
        if (this.streets == null) {
            this.streets = this.dao.getAll();
        }
        return this.streets;
    }

    @Override
    public EntityField<Street, ?> getEntityFieldName() {
        return entityFieldName;
    }

    @Override
    public EntityField<Street, ?> getEntityFieldId() {
        return entityFieldId;
    }

    @Override
    public Class getEntityClass() {
        return Street.class;
    }
}
