package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.MainModel;

public class TariffEntityModel extends BaseEntityModel<Tariff> {

    private List<EntityField> fields;

    public TariffEntityModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("tariff.id", EntityFieldTypes.Integer, Tariff::getId, Tariff::setId, false));
        this.fields.add(new EntityField("tariff.name", EntityFieldTypes.String, Tariff::getName, Tariff::setName));
        this.fields.add(new EntityField("tariff.channels", EntityFieldTypes.String, Tariff::getChannels, Tariff::setChannels));
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
        return "tariff";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public List<Tariff> getListByForeignKey(Integer foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "tariffs";
    }

    @Override
    public Class getEntityClass() {
        return Tariff.class;
    }

    @Override
    protected EntityDAO<Tariff> getDao() {
        return DAOFactory.getDefaultDAOFactory().getTariffDAO();
    }

    @Override
    public Tariff createNewEntity() {
        return new Tariff();
    }
}
