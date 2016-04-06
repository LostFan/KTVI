package org.lostfan.ktv.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.TariffDAO;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.dto.TariffWithPrices;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.TariffSearcherModel;
import org.lostfan.ktv.model.transform.TariffWithPricesTransformer;
import org.lostfan.ktv.validation.TariffPriceValidator;
import org.lostfan.ktv.validation.TariffValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

public class TariffEntityModel extends BaseEntityModel<Tariff> {

    private List<EntityField> fields;

    private Validator<Tariff> validator = new TariffValidator();

    private TariffWithPricesTransformer tariffWithPricesTransformer;
    private TariffPriceValidator tariffPriceValidator;

    public TariffEntityModel() {
        this.fields = new ArrayList<>();
        this.tariffWithPricesTransformer = new TariffWithPricesTransformer();
        this.tariffPriceValidator = new TariffPriceValidator();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("tariff.id", EntityFieldTypes.Integer, Tariff::getId, Tariff::setId, false));
        this.fields.add(new EntityField("tariff.name", EntityFieldTypes.String, Tariff::getName, Tariff::setName));
        this.fields.add(new EntityField("tariff.digital", EntityFieldTypes.Boolean, Tariff::isDigital, Tariff::setDigital));
        this.fields.add(new EntityField("tariff.channels", EntityFieldTypes.String, Tariff::getChannels, Tariff::setChannels));
        this.fields.add(new EntityField("tariff.currentPrice", EntityFieldTypes.Integer, new Function<Tariff, Integer>() {
            @Override
            public Integer apply(Tariff tariff) {
                return getDao().getPriceByDate(tariff.getId(), LocalDate.now());
            }
        }, (e1,e2) -> {}, false));
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(MainModel.getServiceEntityModel());
        entityModels.add(MainModel.getSubscriberEntityModel());
        return entityModels;
    }

    public TariffWithPrices getTariffWithPrices(Integer tariffId) {
        TariffWithPrices tariff = tariffWithPricesTransformer.transformTo(getEntity(tariffId));
        tariff.setArchivePrices(new ArrayList<>());
        List<TariffPrice> prices = getDao().getTariffPrices(tariffId);
        // Sort by date DESC
        prices.stream()
                .sorted((price1, price2) -> {
                    if (price1.getDate().isAfter(price2.getDate())) {
                        return -1;
                    } else if (price1.getDate().equals(price2.getDate())) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .forEach(tariffPrice -> {
                    if (tariffPrice.getDate().isAfter(LocalDate.now())) {
                        tariff.setNewPrice(tariffPrice);
                    } else if (tariff.getCurrentPrice() == null) {
                        tariff.setCurrentPrice(tariffPrice);
                    } else {
                        tariff.getArchivePrices().add(tariffPrice);
                    }
                });
        return tariff;
    }

    public ValidationResult save(TariffPrice price) {
        ValidationResult result = this.tariffPriceValidator.validate(price);
        if (!result.hasErrors()) {
            TariffWithPrices tariffWithPrices = getTariffWithPrices(price.getTariffId());
            if (tariffWithPrices.getNewPrice() != null) {
                getDao().deleteTariffPrice(tariffWithPrices.getNewPrice());
            }
            getDao().saveTariffPrice(price);

        }
        return result;
    }

    public void delete(TariffPrice price) {
        getDao().deleteTariffPrice(price);
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
    public String getEntityNameKey() {
        return "tariffs";
    }

    @Override
    public Class getEntityClass() {
        return Tariff.class;
    }

    @Override
    protected TariffDAO getDao() {
        return DAOFactory.getDefaultDAOFactory().getTariffDAO();
    }

    @Override
    public Tariff createNewEntity() {
        return new Tariff();
    }

    @Override
    public EntitySearcherModel<Tariff> createSearchModel() {
        return new TariffSearcherModel();
    }

    @Override
    public Validator<Tariff> getValidator() {
        return validator;
    }
}
