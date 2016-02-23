package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.DisconnectionReason;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class DisconnectionReasonSearcherModel extends EntitySearcherModel<DisconnectionReason> {

    private List<EntityField> fields;

    public DisconnectionReasonSearcherModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("disconnectionReason.id", EntityFieldTypes.Integer, DisconnectionReason::getId, DisconnectionReason::setId, false));
        this.fields.add(new EntityField("disconnectionReason.name", EntityFieldTypes.String, DisconnectionReason::getName, DisconnectionReason::setName));
    }

    @Override
    public Class getEntityClass() {
        return DisconnectionReason.class;
    }

    @Override
    public String getEntityNameKey() {
        return "disconnectionReasons";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<DisconnectionReason> getDao() {
        return DAOFactory.getDefaultDAOFactory().getDisconnectionReasonDAO();
    }
}
