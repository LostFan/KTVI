package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.TariffDAO;
import org.lostfan.ktv.domain.Tariff;

public class TariffEntityModel extends BaseEntityModel<Tariff> {

    private List<EntityField<Tariff, ?>> fields;

    private TariffDAO dao;
    private List<Tariff> tariffs;

    public TariffEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getTariffDAO();
        fields = new ArrayList<>();


        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("tariff.id", Types.Integer, Tariff::getId, Tariff::setId));
        this.fields.add(new EntityField<>("tariff.name", Types.String, Tariff::getName, Tariff::setName));
        this.fields.add(new EntityField<>("tariff.channels", Types.String, Tariff::getChannels, Tariff::setChannels));
    }

    @Override
    public void saveOrEditEntity(Map<String, Object> collect) {
        Tariff tariff = new Tariff();
        tariff.setChannels((String) collect.get("tariff.channels"));
        tariff.setName((String) collect.get("tariff.name"));
        if(collect.get("tariff.id") != null) {
            Integer tariffId = (Integer) collect.get("tariff.id");
            if (this.dao.getTariff(tariffId) != null) {
                tariff.setId((Integer) collect.get("tariff.id"));
                this.dao.update(tariff);
            } else {
                this.dao.save(tariff);
            }
        } else {
            this.dao.save(tariff);
        }
        this.tariffs = this.dao.getAllTariffs();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.tariffs = this.dao.getAllTariffs();
        this.notifyObservers(null);
    }

    @Override
    public List<EntityComboBoxModel> getEntityComboBoxModels() {
        return null;
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(new ServiceEntityModel());
        entityModels.add(new SubscriberEntityModel());
        return entityModels;
    }

    @Override
    public String getEntityName() {
        return "tariff";
    }

    @Override
    public List<EntityField<Tariff, ?>> getFields() {
        return this.fields;
    }

    public List<Tariff> getList() {
        if (this.tariffs == null) {
            this.tariffs = this.dao.getAllTariffs();
        }

        return this.tariffs;
    }

    @Override
    public String getEntityNameKey() {
        return "tariffs";
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Tariff>> criteria) {
        super.setSearchCriteria(criteria);

        this.tariffs = this.dao.getAllTariffs();
        Stream<Tariff> stream = this.tariffs.stream();
        for (FieldSearchCriterion<Tariff> tariffFieldSearchCriterion : criteria) {
            stream = stream.filter(tariffFieldSearchCriterion.buildPredicate());
        }

        this.tariffs = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return Tariff.class;
    }
}
