package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.MaterialConsumptionDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.dto.AdditionalRenderedService;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.dto.MaterialsDTO;
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

    private EntityField connectionTariffField;
    private EntityField changeTariffField;
    private EntityField serviceEntityField;

    private List<FullEntityField> fullFields;

    private Validator<RenderedService> validator = new RenderedServiceValidator();
    private Validator<SubscriberTariff> validatorSubscriberTariff = new SubscriberTariffValidator();

    private RenderedServiceTransformer transformer = new RenderedServiceTransformer();
    private MaterialConsumptionDAO materialConsumptionDAO = DAOFactory.getDefaultDAOFactory().getMaterialConsumptionDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

    public RenderedServiceEntityModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("renderedService.id", EntityFieldTypes.Integer, RenderedService::getId, RenderedService::setId, false));

        this.fields.add(new EntityField("renderedService", EntityFieldTypes.Service, RenderedService::getServiceId, RenderedService::setServiceId, false));
        this.fields.add(new EntityField("renderedService.date", EntityFieldTypes.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, RenderedService::getSubscriberAccount, RenderedService::setSubscriberAccount));
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Integer, RenderedService::getPrice, RenderedService::setPrice));

        this.serviceEntityField = new EntityField("service", EntityFieldTypes.Service, Service::getId, Service::setId, false);
        this.connectionTariffField = new EntityField("tariff", EntityFieldTypes.Tariff, ConnectionRenderedService::getTariffId, ConnectionRenderedService::setTariffId);
        this.changeTariffField = new EntityField("tariff", EntityFieldTypes.Tariff, ChangeOfTariffRenderedService::getTariffId, ChangeOfTariffRenderedService::setTariffId);

        this.fullFields = new ArrayList<>();

        FullEntityField materialConsumptionField = new FullEntityField("materialConsumption", EntityFieldTypes.MaterialConsumption, MaterialsDTO::getMaterialConsumption, MaterialsDTO::setMaterialConsumption, MaterialConsumption::new);
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

    public DisconnectionRenderedService getDisconnectionRenderedService(RenderedService renderedService) {
        return DisconnectionRenderedService.build(renderedService);
    }

    public ChangeOfTariffRenderedService getChangeOfTariffRenderedService(RenderedService renderedService) {
        SubscriberTariff subscriberTariff = subscriberDAO.getSubscriberTariff(renderedService.getSubscriberAccount(), renderedService.getDate());
        return ChangeOfTariffRenderedService.build(renderedService, subscriberTariff);
    }

    public AdditionalRenderedService getAdditionalRenderedService(RenderedService renderedService) {
        SubscriberTariff subscriberTariff = subscriberDAO.getSubscriberTariff(renderedService.getSubscriberAccount(), renderedService.getDate());
        List<MaterialConsumption> materialConsumptions = materialConsumptionDAO.getMaterialConsumptionsByRenderedServiceId(renderedService.getId());
        return AdditionalRenderedService.build(renderedService, subscriberTariff, materialConsumptions);
    }

    @Override
    public String getEntityName() {
        return "renderedService";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    // TODO: a bad idea to have these methods
    public EntityField getConnectionTariffField() {
        return connectionTariffField;
    }
    public EntityField getChangeTariffField() {
        return changeTariffField;
    }

    public EntityField getServiceField() {
        return this.serviceEntityField;
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

        SubscriberTariff subscriberTariff = new SubscriberTariff();
        subscriberTariff.setTariffId(entity.getTariffId());
        subscriberTariff.setSubscriberAccount(entity.getSubscriberAccount());
        subscriberTariff.setConnectTariff(entity.getDate());
        result = validatorSubscriberTariff.validate(subscriberTariff, result);

        for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
            result = MainModel.getMaterialConsumptionEntityModel().getValidator().validate(materialConsumption, result);
        }
        if (result.hasErrors()) {
            return result;
        }
        SubscriberSession subscriberSession = new SubscriberSession();
        subscriberSession.setConnectionDate(entity.getDate());
        subscriberSession.setSubscriberAccount(entity.getSubscriberAccount());
        if (entity.getId() == null) {
            getDao().save(entity);
            subscriberDAO.saveSubscriberTariff(subscriberTariff);
            subscriberDAO.saveSubscriberSession(subscriberSession);
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
        subscriberDAO.saveSubscriberSession(subscriberSession);
        subscriberDAO.saveSubscriberTariff(subscriberTariff);
        updateMaterials(entity);
        updateEntitiesList();
        return  result;
    }

    public ValidationResult save(DisconnectionRenderedService entity) {
        ValidationResult result = this.getValidator().validate(entity);
        if (result.hasErrors()) {
            return result;
        }


        if (entity.getId() == null) {
            SubscriberSession subscriberSession = subscriberDAO.getNotClosedSubscriberSessionByDate(entity.getSubscriberAccount(), entity.getDate());
            if(subscriberSession == null) {
                result.addError("No session");
                return result;
            }
            subscriberSession.setDisconnectionDate(entity.getDate());
            SubscriberTariff subscriberTariff = subscriberDAO.getNotClosedSubscriberTariffByDate(entity.getSubscriberAccount(), entity.getDate());
            if(subscriberTariff == null) {
                result.addError("No tariff");
                return result;
            }
            subscriberTariff.setDisconnectTariff(entity.getDate());
            getDao().save(entity);

            subscriberDAO.updateSubscriberSession(subscriberSession);
            subscriberDAO.updateSubscriberTariff(subscriberTariff);
        }
        updateEntitiesList();
        return result;
    }

    public ValidationResult save(ChangeOfTariffRenderedService entity) {
        ValidationResult result = this.getValidator().validate(entity);
        if (result.hasErrors()) {
            return result;
        }

        SubscriberTariff newSubscriberTariff = new SubscriberTariff();
        newSubscriberTariff.setTariffId(entity.getTariffId());
        newSubscriberTariff.setSubscriberAccount(entity.getSubscriberAccount());
        newSubscriberTariff.setConnectTariff(entity.getDate());

        if (entity.getId() == null) {
            SubscriberTariff subscriberTariff = subscriberDAO.getNotClosedSubscriberTariffByDate(entity.getSubscriberAccount(), entity.getDate());
            if(subscriberTariff == null) {
                result.addError("No tariff");
                return result;
            }
            subscriberTariff.setDisconnectTariff(entity.getDate());
            getDao().save(entity);

            subscriberDAO.updateSubscriberTariff(subscriberTariff);
            subscriberDAO.saveSubscriberTariff(newSubscriberTariff);
        }
        updateEntitiesList();
        return result;
    }

    public ValidationResult save(AdditionalRenderedService entity) {
        ValidationResult result = this.getValidator().validate(entity);
        for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
            result = MainModel.getMaterialConsumptionEntityModel().getValidator().validate(materialConsumption, result);
        }
        if (result.hasErrors()) {
            return result;
        }
        if (entity.getId() == null) {
            getDao().save(entity);
            for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
                materialConsumption.setRenderedServiceId(entity.getId());
                materialConsumptionDAO.save(materialConsumption);
            }
            return result;
        }
        getDao().update(entity);
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

    private void updateMaterials(AdditionalRenderedService entity) {
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

    public void setServiceFieldEditable(boolean editable) {
        for (EntityField field : fields) {
            if(field.getTitleKey().equals("service")) {
                field.setEditable(editable);
            }
        }
    }

    protected List<RenderedService> getAll() {
        return getDao().getAll().stream().filter(e -> e.getServiceId() != FixedServices.SUBSCRIPTION_FEE.getId()).collect(Collectors.toList());
    }

}
