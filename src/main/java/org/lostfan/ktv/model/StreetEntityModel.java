package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.StreetDAO;
import org.lostfan.ktv.domain.Street;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreetEntityModel extends BaseEntityModel<Street> {

    private List<EntityField> fields;

    private StreetDAO dao;
    private List<Street> streets;

    public StreetEntityModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getStreetDAO();
        fields = new ArrayList<>();


        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("street.id", EntityFieldTypes.Integer, Street::getId, Street::setId));
        this.fields.add(new EntityField("street.name", EntityFieldTypes.String, Street::getName, Street::setName));
    }

    @Override
    public void saveOrEditEntity(Map<String, Object> collect) {
        Street street = new Street();
        street.setName((String) collect.get("street.name"));
        if(collect.get("street.id") != null) {
            Integer streetId = (Integer) collect.get("street.id");
            if (this.dao.get(streetId) != null) {
                street.setId((Integer) collect.get("street.id"));
                this.dao.update(street);
            } else {
                this.dao.save(street);
            }
        } else {
            this.dao.save(street);
        }
        this.streets = this.dao.getAll();
        this.notifyObservers(null);
    }

    @Override
    public void deleteEntityByRow(List<Integer> rowNumbers) {
        for (Integer rowNumber : rowNumbers) {
            int id = getList().get(rowNumber).getId();
            this.dao.delete(id);
        }
        this.streets = this.dao.getAll();
        this.notifyObservers(null);
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

    public List<Street> getList() {
        if (this.streets == null) {
            this.streets = this.dao.getAll();
        }

        return this.streets;
    }

    @Override
    public List<Street> getListByForeignKey(int foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "streets";
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Street>> criteria) {
        super.setSearchCriteria(criteria);

        this.streets = this.dao.getAll();
        Stream<Street> stream = this.streets.stream();
        for (FieldSearchCriterion<Street> streetFieldSearchCriterion : criteria) {
            stream = stream.filter(streetFieldSearchCriterion.buildPredicate());
        }

        this.streets = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }

    @Override
    public Class getEntityClass() {
        return Street.class;
    }
}
