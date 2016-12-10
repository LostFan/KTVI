package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.*;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.RenderedServiceExt;
import org.lostfan.ktv.model.dto.SubscriberSessionDTO;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.SubscriberSearcherModel;
import org.lostfan.ktv.model.transform.PaymentTransformer;
import org.lostfan.ktv.model.transform.RenderedServiceTransformer;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.validation.SubscriberValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

public class SubscriberEntityModel extends BaseEntityModel<Subscriber> {

    private List<EntityField> fields;

    private Validator<Subscriber> validator = new SubscriberValidator();

    private RenderedServiceDAO renderedServiceDAO = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();
    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    private TariffDAO tariffDao = DAOFactory.getDefaultDAOFactory().getTariffDAO();
    private DisconnectionReasonDAO disconnectionReasonDAO = DAOFactory.getDefaultDAOFactory().getDisconnectionReasonDAO();
    private List<Tariff> tariffs = tariffDao.getAll();
    private Map<Integer, Integer> subscribersWithCurrentTariffs = getDao().getSubscribersWithCurrentTariffs();
    private List<Integer> connectedSubscribers = getDao().getConnectedSubscribers();

    private PaymentTransformer paymentTransformer = new PaymentTransformer();
    private RenderedServiceTransformer renderedServiceTransformer = new RenderedServiceTransformer();

    private EntitySearcherModel<Subscriber> searchModel = this.createSearchModel();

    public SubscriberEntityModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("subscriber.account", EntityFieldTypes.Integer, Subscriber::getAccount, Subscriber::setAccount));
        this.fields.add(new EntityField("subscriber.name", EntityFieldTypes.String, Subscriber::getName, Subscriber::setName));
        this.fields.add(new EntityField("subscriber.street_id", EntityFieldTypes.Street, Subscriber::getStreetId, Subscriber::setStreetId));
        this.fields.add(new EntityField("subscriber.house", EntityFieldTypes.Integer, Subscriber::getHouse, Subscriber::setHouse));
        this.fields.add(new EntityField("subscriber.index", EntityFieldTypes.String, Subscriber::getIndex, Subscriber::setIndex));
        this.fields.add(new EntityField("subscriber.building", EntityFieldTypes.String, Subscriber::getBuilding, Subscriber::setBuilding));
        this.fields.add(new EntityField("subscriber.flat", EntityFieldTypes.String, Subscriber::getFlat, Subscriber::setFlat));
        this.fields.add(new EntityField("subscriber.phone", EntityFieldTypes.String, Subscriber::getPhone, Subscriber::setPhone));
        this.fields.add(new EntityField("subscriber.passportNumber", EntityFieldTypes.String, Subscriber::getPassportNumber, Subscriber::setPassportNumber, true ,false));
        this.fields.add(new EntityField("subscriber.passportAuthority", EntityFieldTypes.String, Subscriber::getPassportAuthority, Subscriber::setPassportAuthority, true ,false));
        this.fields.add(new EntityField("subscriber.passportDate", EntityFieldTypes.Date, Subscriber::getPassportDate, Subscriber::setPassportDate, true ,false));
        this.fields.add(new EntityField("subscriber.contractDate", EntityFieldTypes.Date, Subscriber::getContractDate, Subscriber::setContractDate));
        this.fields.add(new EntityField("subscriber.information", EntityFieldTypes.MultilineString, Subscriber::getInformation, Subscriber::setInformation, true, false));
        this.fields.add(new EntityField("tariff", EntityFieldTypes.String, new Function<Subscriber, String>() {
            @Override
            public String apply(Subscriber subscriber) {
                Integer tariffId = subscribersWithCurrentTariffs.get(subscriber.getId());
                if (tariffId == null) {
                    return null;
                }
                if (!connectedSubscribers.contains(subscriber.getAccount())) {
                    return ResourceBundles.getGuiBundle().getString("disconnected");
                }
                return tariffs.stream().filter(e -> tariffId.equals(e.getId())).findFirst().get().getChannels();
            }
        }, (e1, e2) -> {
        }, false));
    }

    @Override
    public ValidationResult save(Subscriber entity) {
        ValidationResult result = getValidator().validate(entity);
        if (result.hasErrors()) {
            return result;
        }
        getDao().save(entity);
        updateEntitiesList();
        return result;
    }

    @Override
    public ValidationResult update(Subscriber entity) {
        ValidationResult result = getValidator().validate(entity);
        if (result.hasErrors()) {
            return result;
        }
        getDao().update(entity);
        updateEntitiesList();
        return result;
    }

    public void updateCollections() {
        tariffs = tariffDao.getAll();
        subscribersWithCurrentTariffs = getDao().getSubscribersWithCurrentTariffs();
        connectedSubscribers = getDao().getConnectedSubscribers();
    }

    @Override
    public String getEntityName() {
        return "subscriber";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "subscribers";
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }


    @Override
    public Subscriber createNewEntity() {
        return new Subscriber();
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }

    @Override
    public Validator<Subscriber> getValidator() {
        return this.validator;
    }

    @Override
    protected SubscriberDAO getDao() {
        return DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    public List<RenderedServiceExt> getRenderedServicesExtBySubscriberId(Integer id) {
        List<RenderedServiceExt> renderedServicesExt = new ArrayList<>();
        for (RenderedService renderedService : renderedServiceDAO.getBySubscriber(id)) {
            RenderedServiceExt renderedServiceExt = renderedServiceTransformer.transformTo(renderedService);
            renderedServiceExt.setService(serviceDAO.get(renderedServiceExt.getServiceId()));
            renderedServicesExt.add(renderedServiceExt);
        }
        return renderedServicesExt;
    }

    public List<PaymentExt> getPaymentsExtBySubscriberId(Integer id) {
        List<PaymentExt> paymentsExt = new ArrayList<>();
        for (Payment payment : paymentDAO.getBySubscriber(id)) {
            PaymentExt paymentExt = paymentTransformer.transformTo(payment);
            paymentExt.setService(serviceDAO.get(paymentExt.getServicePaymentId()));
            paymentsExt.add(paymentExt);
        }
        return paymentsExt;
    }

    @Override
    public EntitySearcherModel<Subscriber> createSearchModel() {
        return new SubscriberSearcherModel();
    }

    public void setSearchQuery(String query) {
        // Replace commas with the space key
        query = query.toLowerCase().replaceAll(",", " ");
        searchModel.setSearchQuery(query);
        this.entities = searchModel.getList();
        this.notifyObservers(null);
    }

    public List<SubscriberTariff> getSubscriberTariffs(Integer subscriberId) {
        return getDao().getSubscriberTariffs(subscriberId);
    }

    public List<SubscriberSession> getSubscriberSessions(Integer subscriberId) {
        return getDao().getSubscriberSessions(subscriberId);
    }

    public List<SubscriberSessionDTO> getSubscriberSessionDTOs(Integer subscriberId) {
        List<SubscriberSessionDTO> subscriberSessionDTOs = new ArrayList<>();
        for (SubscriberSession subscriberSession : getDao().getSubscriberSessions(subscriberId)) {
            SubscriberSessionDTO subscriberSessionDTO = new SubscriberSessionDTO(subscriberSession);
            if(subscriberSession.getDisconnectionReasonId() != null) {
                subscriberSessionDTO.setDisconnectionReason(disconnectionReasonDAO.get(subscriberSession.getDisconnectionReasonId()).getName());
            }
            subscriberSessionDTOs.add(subscriberSessionDTO);
        }
        return subscriberSessionDTOs;
    }

    public Integer getNewSubscriberAccount() {
        Integer lastAccount = getDao().getLastSubscriberAccount();
        Integer newAccount = lastAccount / 10 * 10 + 10 + (int) (Math.random() * 10);
        return newAccount;
    }
}
