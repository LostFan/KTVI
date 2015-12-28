package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class TariffSearcherModel extends EntitySearcherModel<Tariff> {

    private List<EntityField> fields;

    public TariffSearcherModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("tariff.id", EntityFieldTypes.Integer, Tariff::getId, Tariff::setId, false));
        this.fields.add(new EntityField("tariff.name", EntityFieldTypes.String, Tariff::getName, Tariff::setName));
        this.fields.add(new EntityField("tariff.digital", EntityFieldTypes.Boolean, Tariff::isDigital, Tariff::setDigital));
        this.fields.add(new EntityField("tariff.channels", EntityFieldTypes.String, Tariff::getChannels, Tariff::setChannels));
    }

    @Override
    public Class getEntityClass() {
        return Tariff.class;
    }

    @Override
    public String getEntityNameKey() {
        return "tariffs";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<Tariff> getDao() {
        return DAOFactory.getDefaultDAOFactory().getTariffDAO();
    }
}
