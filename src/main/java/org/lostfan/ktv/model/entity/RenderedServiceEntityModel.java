package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.*;
import org.lostfan.ktv.model.*;
import org.lostfan.ktv.model.dto.AdditionalRenderedService;
import org.lostfan.ktv.model.dto.ChangeOfTariffRenderedService;
import org.lostfan.ktv.model.dto.ConnectionRenderedService;
import org.lostfan.ktv.model.dto.DisconnectionRenderedService;
import org.lostfan.ktv.model.dto.MaterialsDTO;
import org.lostfan.ktv.validation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RenderedServiceEntityModel extends BaseEntityModel<RenderedService> {

    private LocalDate date;
    private List<EntityField> fields;

    private EntityField connectionTariffField;
    private EntityField changeTariffField;
    private EntityField serviceEntityField;

    private List<FullEntityField> fullFields;

    private Validator<RenderedService> validator = new RenderedServiceValidator();
    private Validator<SubscriberTariff> validatorSubscriberTariff = new SubscriberTariffValidator();
    private Validator<RenderedService> connectionAdditionValidator = new ConnectionAdditionValidator();
    private ConnectionEditValidator connectionEditValidator = new ConnectionEditValidator();
    private DisconnectionAdditionValidator disconnectionAdditionValidator = new DisconnectionAdditionValidator();
    private DisconnectionEditValidator disconnectionEditValidator = new DisconnectionEditValidator();
    private ChangeTariffAdditionValidator changeTariffAdditionValidator = new ChangeTariffAdditionValidator();
    private ChangeTariffEditValidator changeTariffEditValidator = new ChangeTariffEditValidator();
    private ConnectionDeleteValidator connectionDeleteValidator = new ConnectionDeleteValidator();
    private DisconnectionDeleteValidator disconnectionDeleteValidator = new DisconnectionDeleteValidator();
    private ChangeTariffDeleteValidator changeTariffDeleteValidator = new ChangeTariffDeleteValidator();

    private MaterialConsumptionDAO materialConsumptionDAO = DAOFactory.getDefaultDAOFactory().getMaterialConsumptionDAO();
    private MaterialDAO materialDAO = DAOFactory.getDefaultDAOFactory().getMaterialDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();

    public RenderedServiceEntityModel() {
        date = LocalDate.now().withDayOfMonth(1);

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("renderedService.id", EntityFieldTypes.Integer, RenderedService::getId, RenderedService::setId, false));

        this.fields.add(new EntityField("renderedService", EntityFieldTypes.Service, RenderedService::getServiceId, RenderedService::setServiceId, false));
        this.fields.add(new EntityField("renderedService.date", EntityFieldTypes.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, RenderedService::getSubscriberAccount, RenderedService::setSubscriberAccount));
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Integer, RenderedService::getPrice, RenderedService::setPrice));

        this.serviceEntityField = new EntityField("service", EntityFieldTypes.Service, AdditionalRenderedService::getServiceId, AdditionalRenderedService::setServiceId, false);
        this.connectionTariffField = new EntityField("tariff", EntityFieldTypes.Tariff, ConnectionRenderedService::getTariffId, ConnectionRenderedService::setTariffId);
        this.changeTariffField = new EntityField("tariff", EntityFieldTypes.Tariff, ChangeOfTariffRenderedService::getTariffId, ChangeOfTariffRenderedService::setTariffId);

        this.fullFields = new ArrayList<>();

        FullEntityField materialConsumptionField = new FullEntityField("materialConsumption", EntityFieldTypes.MaterialConsumption, MaterialsDTO::getMaterialConsumption, MaterialsDTO::setMaterialConsumption, MaterialConsumption::new);
        List<EntityField> entityFields = MainModel.getMaterialConsumptionEntityModel().getFields().stream().filter(e -> !e.getTitleKey().equals("renderedService")).filter(e -> !e.getTitleKey().equals("materialConsumption.id")).collect(Collectors.toList());
        entityFields.add(new EntityField("materialConsumption.price", EntityFieldTypes.Integer, new Function<MaterialConsumption, Integer>() {
            @Override
            public Integer apply(MaterialConsumption materialConsumption) {
                if (materialConsumption.getMaterialId() == null) {
                    return null;
                }
                return materialDAO.get(materialConsumption.getMaterialId()).getPrice();
            }
        }, (e1, e2) -> {
        }));
        entityFields.add(new EntityField("materialConsumption.allPrice", EntityFieldTypes.Integer, new Function<MaterialConsumption, Integer>() {
            @Override
            public Integer apply(MaterialConsumption materialConsumption) {
                if (materialConsumption.getMaterialId() == null
                        || materialConsumption.getAmount() == null) {
                    return null;
                }
                return ((Double) (materialDAO.get(materialConsumption.getMaterialId()).getPrice() * materialConsumption.getAmount())).intValue();
            }
        }, (e1, e2) -> {
        }));
        materialConsumptionField.setEntityFields(entityFields);

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
        SubscriberTariff subscriberTariff = subscriberDAO.getSubscriberTariffAtDate(renderedService.getSubscriberAccount(), renderedService.getDate());
        List<MaterialConsumption> materialConsumptions = materialConsumptionDAO.getByRenderedServiceId(renderedService.getId());
        return ConnectionRenderedService.build(renderedService, subscriberTariff, materialConsumptions);
    }

    public DisconnectionRenderedService getDisconnectionRenderedService(RenderedService renderedService) {
        return DisconnectionRenderedService.build(renderedService);
    }

    public ChangeOfTariffRenderedService getChangeOfTariffRenderedService(RenderedService renderedService) {
        SubscriberTariff subscriberTariff = subscriberDAO.getSubscriberTariffAtDate(renderedService.getSubscriberAccount(), renderedService.getDate());
        return ChangeOfTariffRenderedService.build(renderedService, subscriberTariff);
    }

    public AdditionalRenderedService getAdditionalRenderedService(RenderedService renderedService) {
        SubscriberTariff subscriberTariff = subscriberDAO.getSubscriberTariffAtDate(renderedService.getSubscriberAccount(), renderedService.getDate());
        List<MaterialConsumption> materialConsumptions = materialConsumptionDAO.getByRenderedServiceId(renderedService.getId());
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
            result = this.connectionAdditionValidator.validate(entity, result);
            if (result.hasErrors()) {
                return result;
            }
            getDao().save(entity);
            SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffAtDate(entity.getSubscriberAccount(), entity.getDate());
            if (oldSubscriberTariff == null) {
                subscriberDAO.saveSubscriberTariff(subscriberTariff);
            }
            subscriberDAO.saveSubscriberSession(subscriberSession);
//            for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
//                materialConsumption.setRenderedServiceId(entity.getId());
//                materialConsumptionDAO.save(materialConsumption);
//            }
            updateEntitiesList();
            return result;
        }

        RenderedService prevRenderedService = getDao().get(entity.getId());
        if (!prevRenderedService.getSubscriberAccount().equals(entity.getSubscriberAccount())) {
            result = this.connectionAdditionValidator.validate(entity, result);
        }
        result = this.connectionEditValidator.validate(entity, prevRenderedService, result);

        if (result.hasErrors()) {
            return result;
        }

        subscriberDAO.deleteSubscriberSession(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffAtDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        if (prevRenderedService.getDate().equals(oldSubscriberTariff.getConnectTariff())) {
            subscriberDAO.deleteSubscriberTariff(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        }
        getDao().update(entity);
        subscriberDAO.saveSubscriberSession(subscriberSession);
        if (subscriberDAO.getNotClosedSubscriberTariff(entity.getSubscriberAccount(), entity.getDate()) == null) {
            subscriberDAO.saveSubscriberTariff(subscriberTariff);
        }
//        updateMaterials(entity);
        updateEntitiesList();
        return result;
    }

    public ValidationResult save(DisconnectionRenderedService entity) {
        ValidationResult result = this.getValidator().validate(entity);
        if (result.hasErrors()) {
            return result;
        }

        if (entity.getId() == null) {
            result = this.disconnectionAdditionValidator.validate(entity, result);
            if (result.hasErrors()) {
                return result;
            }
            SubscriberSession subscriberSession = subscriberDAO.getNotClosedSubscriberSession(entity.getSubscriberAccount(), entity.getDate());
            subscriberSession.setDisconnectionDate(entity.getDate());

            getDao().save(entity);
            subscriberDAO.updateSubscriberSession(subscriberSession);
            updateEntitiesList();
            return result;
        }

        RenderedService prevRenderedService = getDao().get(entity.getId());

        if (!prevRenderedService.getSubscriberAccount().equals(entity.getSubscriberAccount())) {
            result = this.disconnectionAdditionValidator.validate(entity, result);
        }
        result = this.disconnectionEditValidator.validate(entity, prevRenderedService, result) ;

        if (result.hasErrors()) {
            return result;
        }

        SubscriberSession currentSession = subscriberDAO.getSubscriberSessionByDisconnectionDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        currentSession.setDisconnectionDate(null);
        currentSession.setDisconnectionReasonId(null);
        subscriberDAO.updateSubscriberSession(currentSession);
        SubscriberSession newSession = subscriberDAO.getNotClosedSubscriberSession(entity.getSubscriberAccount(), entity.getDate());
        newSession.setDisconnectionDate(entity.getDate());
        getDao().update(entity);
        subscriberDAO.updateSubscriberSession(newSession);


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

            result = this.changeTariffAdditionValidator.validate(entity, result);
            if (result.hasErrors()) {
                return result;
            }

            SubscriberTariff subscriberTariff = subscriberDAO.getNotClosedSubscriberTariff(entity.getSubscriberAccount(), entity.getDate());
            subscriberTariff.setDisconnectTariff(entity.getDate());

            getDao().save(entity);
            subscriberDAO.updateSubscriberTariff(subscriberTariff);
            subscriberDAO.saveSubscriberTariff(newSubscriberTariff);
            updateEntitiesList();
            return result;
        }

        RenderedService prevRenderedService = getDao().get(entity.getId());

        if (!(prevRenderedService.getSubscriberAccount().equals(entity.getSubscriberAccount()))) {
            result = this.changeTariffAdditionValidator.validate(entity, result);
        }
        result = this.changeTariffEditValidator.validate(entity, prevRenderedService, result);

        if (result.hasErrors()) {
            return result;
        }

        subscriberDAO.deleteSubscriberTariff(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffByDisconnectionDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        oldSubscriberTariff.setDisconnectTariff(null);
        subscriberDAO.updateSubscriberTariff(oldSubscriberTariff);
        getDao().update(entity);
        SubscriberTariff notClosedSubscriberTariffNewSubscriber = subscriberDAO.getNotClosedSubscriberTariff(entity.getSubscriberAccount(), entity.getDate());
        notClosedSubscriberTariffNewSubscriber.setDisconnectTariff(newSubscriberTariff.getConnectTariff());
        subscriberDAO.updateSubscriberTariff(notClosedSubscriberTariffNewSubscriber);
        subscriberDAO.saveSubscriberTariff(newSubscriberTariff);
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
//            for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
//                materialConsumption.setRenderedServiceId(entity.getId());
//                materialConsumptionDAO.save(materialConsumption);
//            }
            return result;
        }
        getDao().update(entity);
//        updateMaterials(entity);
        updateEntitiesList();
        return result;
    }


    public ValidationResult deleteRenderedServicesById(List<Integer> ids) {
        ValidationResult result = ValidationResult.createEmpty();
        if (ids.size() == 0) {
            return result;
        }

        for (Integer id : ids) {
            RenderedService entity = getDao().get(id);
            if(entity.getServiceId().equals(FixedServices.CONNECTION.getId())) {
                isDeleteConnection(entity, result);
            }
            if(entity.getServiceId().equals(FixedServices.DISCONNECTION.getId())) {
                isDeleteDisconnection(entity, result);
            }
            if(entity.getServiceId().equals(FixedServices.CHANGE_OF_TARIFF.getId())) {
                isDeleteChangeTariff(entity, result);
            }
//            getDao().delete(id);
        }
        if (result.hasErrors()) {
            return result;
        }
        for (Integer id : ids) {
            RenderedService entity = getDao().get(id);
            if(entity.getServiceId().equals(FixedServices.CONNECTION.getId())) {
                deleteConnection(entity);
            }
            if(entity.getServiceId().equals(FixedServices.DISCONNECTION.getId())) {
                deleteDisconnection(entity);
            }
            if(entity.getServiceId().equals(FixedServices.CHANGE_OF_TARIFF.getId())) {
                deleteChangeTariff(entity);
            }

        }
        updateEntitiesList();
        return result;
    }

    private ValidationResult isDeleteConnection(RenderedService entity, ValidationResult result) {
        connectionDeleteValidator.validate(entity, result);
        return result;
    }

    private void deleteConnection(RenderedService entity) {
        subscriberDAO.deleteSubscriberSession(entity.getSubscriberAccount(), entity.getDate());
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffAtDate(entity.getSubscriberAccount(), entity.getDate());
        if (entity.getDate().equals(oldSubscriberTariff.getConnectTariff())) {
            subscriberDAO.deleteSubscriberTariff(entity.getSubscriberAccount(), entity.getDate());
        }
        getDao().delete(entity.getId());
    }

    private ValidationResult isDeleteDisconnection(RenderedService entity, ValidationResult result) {
        disconnectionDeleteValidator.validate(entity, result);
        return result;
    }

    private void deleteDisconnection(RenderedService entity) {
        SubscriberSession currentSession = subscriberDAO.getSubscriberSessionByDisconnectionDate(entity.getSubscriberAccount(), entity.getDate());
        currentSession.setDisconnectionDate(null);
        currentSession.setDisconnectionReasonId(null);
        subscriberDAO.updateSubscriberSession(currentSession);
        getDao().delete(entity.getId());
    }

    private ValidationResult isDeleteChangeTariff(RenderedService entity, ValidationResult result) {
        changeTariffDeleteValidator.validate(entity, result);
        return result;
    }

    private void deleteChangeTariff(RenderedService entity) {
        subscriberDAO.deleteSubscriberTariff(entity.getSubscriberAccount(), entity.getDate());
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffByDisconnectionDate(entity.getSubscriberAccount(), entity.getDate());
        oldSubscriberTariff.setDisconnectTariff(null);
        subscriberDAO.updateSubscriberTariff(oldSubscriberTariff);
        getDao().delete(entity.getId());
    }

    @Override
    protected RenderedServiceDAO getDao() {
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

    private void updateMaterials(MaterialsDTO entity) {
        List<MaterialConsumption> materialConsumptionList = materialConsumptionDAO.getByRenderedServiceId(entity.getId());

        for (MaterialConsumption materialConsumption : materialConsumptionList) {
            if (!entity.getMaterialConsumption().contains(materialConsumption)) {
                materialConsumptionDAO.delete(materialConsumption.getId());
            }
        }

        for (MaterialConsumption materialConsumption : entity.getMaterialConsumption()) {
            if (materialConsumption.getId() != null) {
                materialConsumptionDAO.update(materialConsumption);
            } else {
                materialConsumption.setRenderedServiceId(entity.getId());
                materialConsumptionDAO.save(materialConsumption);
            }
        }
    }

    public void setServiceFieldEditable(boolean editable) {
        for (EntityField field : fields) {
            if (field.getTitleKey().equals("service")) {
                field.setEditable(editable);
            }
        }
    }

    public Integer getRenderedServicePriceByDate(Integer serviceId, LocalDate date) {
        if (serviceId != null && date != null) {
            return serviceDAO.getPriceByDate(serviceId, date);
        }
        return 0;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        updateEntitiesList();
    }

    public List<RenderedService> getAll() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO().getByMonth(this.date)
                .stream().filter(e -> e.getServiceId() != FixedServices.SUBSCRIPTION_FEE.getId()).collect(Collectors.toList());
    }

    public LocalDate getDate() {
        return this.date;
    }
}
