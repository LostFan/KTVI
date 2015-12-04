package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.MaterialConsumptionDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.transform.RenderedServiceTransformer;
import org.lostfan.ktv.validation.RenderedServiceValidator;
import org.lostfan.ktv.validation.SubscriberTariffValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RenderedServiceEntityModel extends BaseEntityModel<RenderedService> {

    private List<EntityField> fields;

    private EntityField tariffField;

    private List<FullEntityField> fullFields;

    private Validator<RenderedService> validator = new RenderedServiceValidator();
    private Validator<SubscriberTariff> validatorSubscriberTariff = new SubscriberTariffValidator();

    private RenderedServiceTransformer transformer = new RenderedServiceTransformer();
    private MaterialConsumptionDAO materialConsumptionDAO = DAOFactory.getDefaultDAOFactory().getMaterialConsumptionDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

    private SubscriberTariff subscriberTariff;


    public RenderedServiceEntityModel() {
        this.fields = new ArrayList<>();
        EntityField serviceEntityField = new EntityField("renderedService.id", EntityFieldTypes.Integer, RenderedService::getId, RenderedService::setId, false);
        this.fields.add(serviceEntityField);
        this.fields.add(new EntityField("renderedService.date", EntityFieldTypes.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, RenderedService::getSubscriberAccount, RenderedService::setSubscriberAccount));
//        this.fields.add(new EntityField("service", EntityFieldTypes.Service, RenderedService::getServiceId, RenderedService::setServiceId));
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Integer, RenderedService::getPrice, RenderedService::setPrice));

        this.tariffField = new EntityField("tariff", EntityFieldTypes.Tariff, Tariff::getId, Tariff::setId);

        this.fullFields = new ArrayList<>();

        FullEntityField materialConsumptionField = new FullEntityField("materialConsumption", EntityFieldTypes.MaterialConsumption, ConnectionRenderedService::getMaterialConsumption, ConnectionRenderedService::setMaterialConsumption, MaterialConsumption::new);
        materialConsumptionField.setEntityFields(MainModel.getMaterialConsumptionEntityModel().getFields().stream().filter(e -> !e.getTitleKey().equals("renderedService")).filter(e -> !e.getTitleKey().equals("materialConsumption.id")).collect(Collectors.toList()));
        this.fullFields.add(materialConsumptionField);
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(MainModel.getServiceEntityModel());
        entityModels.add(MainModel.getSubscriberEntityModel());
        return entityModels;
    }

    public ConnectionRenderedService getConnectionRenderedService(RenderedService renderedService) {
        SubscriberTariff subscriberTariff = subscriberDAO.getSubscriberTariff(renderedService.getSubscriberAccount(), renderedService.getDate());
        List<MaterialConsumption> materialConsumptions = materialConsumptionDAO.getMaterialConsumptionsByRenderedServiceId(renderedService.getId());

        return ConnectionRenderedService.build(renderedService, subscriberTariff, materialConsumptions);
    }

    @Override
    public String getEntityName() {
        return "renderedService";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    public EntityField getTariffField() {
        return this.tariffField;
    }

    @Override
    public List<FullEntityField> getFullFields() {
        return this.fullFields;
    }

    @Override
    public String getEntityNameKey() {
        return "renderedServices";
    }

    @Override
    public Class getEntityClass() {
        return RenderedService.class;
    }


    public ValidationResult save(ConnectionRenderedService entity) {
        ValidationResult result = this.getValidator().validate(entity);
        result = validatorSubscriberTariff.validate(this.subscriberTariff, result);
        for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
            result = MainModel.getMaterialConsumptionEntityModel().getValidator().validate(materialConsumption, result);
        }
        if (result.hasErrors()) {
            return result;
        }

        if (entity.getId() == null) {
            getDao().save(entity);
            subscriberDAO.updateSubscriberTariff(this.subscriberTariff);
            for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
                materialConsumption.setRenderedServiceId(entity.getId());
                materialConsumptionDAO.save(materialConsumption);
            }
            return result;
        }
        RenderedService prevRenderedService = getDao().get(entity.getId());
        subscriberDAO.deleteSubscriberSession(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        subscriberDAO.deleteSubscriberTariff(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        getDao().update(entity);
        subscriberDAO.saveSubscriberSession(entity.getSubscriberAccount(), entity.getDate());
        subscriberDAO.saveSubscriberTariff(this.subscriberTariff);
        updateMaterials(entity);
        updateEntitiesList();
        return  result;
    }

    @Override
    protected EntityDAO<RenderedService> getDao() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    }

    @Override
    public RenderedService createNewEntity() {
        return new RenderedService();
    }

    @Override
    public Validator<RenderedService> getValidator() {
        return this.validator;
    }


    public ConnectionRenderedService buildDTO(RenderedService service, Tariff tariff, Map<String, List<Entity>> map) {
        ConnectionRenderedService dto = new ConnectionRenderedService();
        dto.setId(service.getId());
        dto.setDate(service.getDate());
        dto.setPrice(service.getPrice());
        dto.setSubscriberAccount(service.getSubscriberAccount());
        dto.setServiceId(service.getServiceId());
        dto.setTariffId(tariff.getId());
        dto.setMaterialConsumption(map.get("materialConsumption").stream().map(e -> (MaterialConsumption) e).collect(Collectors.toList()));
        this.subscriberTariff = new SubscriberTariff();
        this.subscriberTariff.setTariffId(tariff.getId());
        this.subscriberTariff.setSubscriberAccount(service.getSubscriberAccount());
        this.subscriberTariff.setConnectTariff(service.getDate());
        return dto;

    }

    private void updateMaterials(ConnectionRenderedService entity) {
        List<MaterialConsumption> materialConsumptionList = materialConsumptionDAO.getMaterialConsumptionsByRenderedServiceId(entity.getId());

        for (MaterialConsumption materialConsumption : materialConsumptionList) {
            if(!entity.getMaterialConsumption().contains(materialConsumption)) {
                materialConsumptionDAO.delete(materialConsumption.getId());
            }
        }

        for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
            if(materialConsumption.getId() != null) {
                materialConsumptionDAO.update(materialConsumption);
            } else {
                materialConsumption.setRenderedServiceId(entity.getId());
                materialConsumptionDAO.save(materialConsumption);
            }
        }
    }

}
