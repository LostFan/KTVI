package org.lostfan.ktv.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.RenderedServiceExt;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.SubscriberSearchCriteria;
import org.lostfan.ktv.model.searcher.SubscriberSearcherModel;
import org.lostfan.ktv.model.transform.PaymentTransformer;
import org.lostfan.ktv.model.transform.RenderedServiceTransformer;
import org.lostfan.ktv.validation.SubscriberValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.validation.Validator;

public class SubscriberEntityModel extends BaseEntityModel<Subscriber> {

    private enum QueryParseStates {
        Begin,
        Name,
        House,
        Flat,
        End;
    }

    private List<EntityField> fields;

    private Validator<Subscriber> validator = new SubscriberValidator();

    private RenderedServiceDAO renderedServiceDAO = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();
    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();

    private PaymentTransformer paymentTransformer = new PaymentTransformer();
    private RenderedServiceTransformer renderedServiceTransformer = new RenderedServiceTransformer();

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
        this.fields.add(new EntityField("subscriber.passportNumber", EntityFieldTypes.String, Subscriber::getPassportNumber, Subscriber::setPassportNumber));
        this.fields.add(new EntityField("subscriber.passportAuthority", EntityFieldTypes.String, Subscriber::getPassportAuthority, Subscriber::setPassportAuthority));
        this.fields.add(new EntityField("subscriber.passportDate", EntityFieldTypes.Date, Subscriber::getPassportDate, Subscriber::setPassportDate));
        this.fields.add(new EntityField("subscriber.contractDate", EntityFieldTypes.Date, Subscriber::getContractDate, Subscriber::setContractDate));
        this.fields.add(new EntityField("subscriber.information", EntityFieldTypes.MultilineString, Subscriber::getInformation, Subscriber::setInformation));
//        this.fields.add(new EntityField("subscriber.balance", EntityFieldTypes.Integer, Subscriber::getBalance, Subscriber::setBalance));
//        this.fields.add(new EntityField("subscriber.connected", EntityFieldTypes.Boolean, Subscriber::isConnected, Subscriber::setConnected));
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
        this.entities = getDao().search(parseQuery(query));
        this.notifyObservers(null);
    }

    /**
     * A smart search implementation
     */
    private SubscriberSearchCriteria parseQuery(String query) {
        SubscriberSearchCriteria criteria = new SubscriberSearchCriteria();

        StringTokenizer tokenizer = new StringTokenizer(query, " ");

        // The query contains just one word
        if (tokenizer.countTokens() == 1) {
            String name = tokenizer.nextToken();
            criteria.addName(name);
            criteria.addStreet(name);
            return criteria;
        }

        QueryParseStates lastState = QueryParseStates.Begin;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            switch (lastState) {
                case Begin:
                    criteria.addName(token);
                    criteria.addStreet(token);
                    lastState = QueryParseStates.Name;
                    break;
                case Name:
                    // Try to retrieve a house number
                    int i;
                    for (i = 0; i < token.length(); ++i) {
                        if (!Character.isDigit(token.charAt(i))) {
                            break;
                        }
                    }
                    // House numbers start with a digit
                    if (i != 0) {
                        criteria.setHouse(Integer.parseInt(token.substring(0, i)));
                        if (i != token.length()) {
                            criteria.setIndex(token.substring(i, token.length()));
                        }
                        // remove subscriber name from the criteria
                        criteria.setNameIn(new ArrayList<>());
                        lastState = QueryParseStates.House;

                    }
                    // Otherwise it's another word of a name
                    else {
                        criteria.addStreet(token);
                        criteria.addName(token);
                    }
                    break;
                case House:
                    // Next token is a flat number
                    criteria.setFlat(token);
                    lastState = QueryParseStates.Flat;
                    break;
                case Flat:
                    // Possibly the "flat" value is actually a building value
                    // And the next value is true flat value
                    criteria.setBuilding(criteria.getFlat());
                    criteria.setFlat(token);
                    break;
                case End:
                    // ???
                    // What to do next?
                    break;
            }

        }

        return criteria;
    }

    public List<SubscriberTariff> getSubscriberTariffs(Integer subscriberId) {
        return getDao().getSubscriberTariffs(subscriberId);
    }

    public List<SubscriberSession> getSubscriberSessions(Integer subscriberId) {
        return getDao().getSubscriberSessions(subscriberId);
    }
}
