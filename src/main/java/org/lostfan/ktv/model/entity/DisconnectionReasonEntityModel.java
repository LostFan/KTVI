package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.DisconnectionReason;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;

import java.util.ArrayList;
import java.util.List;

public class DisconnectionReasonEntityModel extends BaseEntityModel<DisconnectionReason> {

    private List<EntityField> fields;

    public DisconnectionReasonEntityModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("disconnectionReason.id", EntityFieldTypes.Integer, DisconnectionReason::getId, DisconnectionReason::setId, false));
        this.fields.add(new EntityField("disconnectionReason.name", EntityFieldTypes.String, DisconnectionReason::getName, DisconnectionReason::setName));
    }

    @Override
    protected EntityDAO<DisconnectionReason> getDao() {
        return DAOFactory.getDefaultDAOFactory().getDisconnectionReasonDAO();
    }

    @Override
    public String getEntityNameKey() {
        return "disconnectionReasons";
    }

    @Override
    public String getEntityName() {
        return "disconnectionReason";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public DisconnectionReason createNewEntity() {
        return new DisconnectionReason();
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return DisconnectionReason.class;
    }

    @Override
    public EntitySearcherModel<DisconnectionReason> createSearchModel() {
        return null;
    }
}
