package org.lostfan.ktv.model.entity;


import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class TariffPriceEntityModel extends BaseEntityModel<TariffPrice> {

    private List<EntityField> fields;

    public TariffPriceEntityModel() {
        fields = new ArrayList<>();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("tariffPrice.id", EntityFieldTypes.Integer, TariffPrice::getId, TariffPrice::setId, false));
        this.fields.add(new EntityField("tariffPrice.date", EntityFieldTypes.Date, TariffPrice::getDate, TariffPrice::setDate));
        this.fields.add(new EntityField("tariff", EntityFieldTypes.Tariff, TariffPrice::getTariffId, TariffPrice::setTariffId));
        this.fields.add(new EntityField("tariffPrice.price", EntityFieldTypes.Integer, TariffPrice::getPrice, TariffPrice::setPrice));
    }

    @Override
    protected EntityDAO<TariffPrice> getDao() {
        return DAOFactory.getDefaultDAOFactory().getTariffPriceDAO();
    }

    @Override
    public List<TariffPrice> getListByForeignKey(Integer foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "tariffPrice";
    }

    @Override
    public String getEntityName() {
        return "tariffPrice";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public TariffPrice createNewEntity() {
        return new TariffPrice();
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return TariffPrice.class;
    }
}
