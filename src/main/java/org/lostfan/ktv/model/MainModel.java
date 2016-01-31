package org.lostfan.ktv.model;

import org.lostfan.ktv.model.entity.*;
import org.lostfan.ktv.utils.BaseObservable;

import java.util.*;
import java.util.stream.Collectors;

public class MainModel extends BaseObservable {

    private static Map<String, EntityModel> nameEntityModels;
    private static Map<Class, EntityModel> classEntityModels;

    private static DisconnectionReasonEntityModel disconnectionReasonEntityModel;
    private static MaterialConsumptionEntityModel materialConsumptionEntityModel;
    private static MaterialEntityModel materialEntityModel;
    private static PaymentEntityModel paymentEntityModel;
    private static RenderedServiceEntityModel renderedServiceEntityModel;
    private static SubscriptionFeeModel subscriptionFeeModel;
    private static ServiceEntityModel serviceEntityModel;
    private static StreetEntityModel streetEntityModel;
    private static SubscriberEntityModel subscriberEntityModel;
    private static TariffEntityModel tariffEntityModel;

    static {
        disconnectionReasonEntityModel = new DisconnectionReasonEntityModel();
        materialConsumptionEntityModel = new MaterialConsumptionEntityModel();
        materialEntityModel = new MaterialEntityModel();
        paymentEntityModel = new PaymentEntityModel();
        renderedServiceEntityModel = new RenderedServiceEntityModel();
        subscriptionFeeModel = new SubscriptionFeeModel();
        serviceEntityModel = new ServiceEntityModel();
        streetEntityModel = new StreetEntityModel();
        subscriberEntityModel = new SubscriberEntityModel();
        tariffEntityModel = new TariffEntityModel();

        nameEntityModels = new HashMap<>();
        nameEntityModels.put(disconnectionReasonEntityModel.getEntityNameKey(), disconnectionReasonEntityModel);
        nameEntityModels.put(materialConsumptionEntityModel.getEntityNameKey(), materialConsumptionEntityModel);
        nameEntityModels.put(materialEntityModel.getEntityNameKey(), materialEntityModel);
        nameEntityModels.put(paymentEntityModel.getEntityNameKey(), paymentEntityModel);
        nameEntityModels.put(renderedServiceEntityModel.getEntityNameKey(), renderedServiceEntityModel);
//        nameEntityModels.put(subscriptionFeeModel.getEntityNameKey(), subscriptionFeeModel);
        nameEntityModels.put(serviceEntityModel.getEntityNameKey(), serviceEntityModel);
        nameEntityModels.put(streetEntityModel.getEntityNameKey(), streetEntityModel);
        nameEntityModels.put(subscriberEntityModel.getEntityNameKey(), subscriberEntityModel);
        nameEntityModels.put(tariffEntityModel.getEntityNameKey(), tariffEntityModel);

        classEntityModels = new HashMap<>();
        classEntityModels.put(disconnectionReasonEntityModel.getEntityClass(), disconnectionReasonEntityModel);
        classEntityModels.put(materialConsumptionEntityModel.getEntityClass(), materialConsumptionEntityModel);
        classEntityModels.put(materialEntityModel.getEntityClass(), materialEntityModel);
        classEntityModels.put(paymentEntityModel.getEntityClass(), paymentEntityModel);
        classEntityModels.put(renderedServiceEntityModel.getEntityClass(), renderedServiceEntityModel);
        classEntityModels.put(serviceEntityModel.getEntityClass(), serviceEntityModel);
        classEntityModels.put(streetEntityModel.getEntityClass(), streetEntityModel);
        classEntityModels.put(subscriberEntityModel.getEntityClass(), subscriberEntityModel);
        classEntityModels.put(tariffEntityModel.getEntityClass(), tariffEntityModel);
    }

    public static EntityModel getEntityModel(String entityName) {
        return nameEntityModels.get(entityName);
    }

    public static EntityModel getEntityModel(Class entityClass) {
        return classEntityModels.get(entityClass);
    }

    public static MaterialConsumptionEntityModel getMaterialConsumptionEntityModel() {
        return materialConsumptionEntityModel;
    }

    public static DisconnectionReasonEntityModel getDisconnectionReasonEntityModel() {
        return disconnectionReasonEntityModel;
    }

    public static MaterialEntityModel getMaterialEntityModel() {
        return materialEntityModel;
    }

    public static PaymentEntityModel getPaymentEntityModel() {
        return paymentEntityModel;
    }

    public static RenderedServiceEntityModel getRenderedServiceEntityModel() {
        return renderedServiceEntityModel;
    }

    public static SubscriptionFeeModel getSubscriptionFeeModel() {
        return subscriptionFeeModel;
    }

    public static ServiceEntityModel getServiceEntityModel() {
        return serviceEntityModel;
    }

    public static StreetEntityModel getStreetEntityModel() {
        return streetEntityModel;
    }

    public static SubscriberEntityModel getSubscriberEntityModel() {
        return subscriberEntityModel;
    }

    public static TariffEntityModel getTariffEntityModel() {
        return tariffEntityModel;
    }

    private List<String> entityModelNames;
    private List<String> documentModelNames;
    private List<String> reportNames;
    private List<String> servicesNames;
    private BaseModel currentModel;

    public MainModel() {
        this.entityModelNames = new ArrayList<>();
        this.documentModelNames = new ArrayList<>();

        this.entityModelNames.add(getServiceEntityModel().getEntityNameKey());
        this.entityModelNames.add(getSubscriberEntityModel().getEntityNameKey());
        this.entityModelNames.add(getMaterialEntityModel().getEntityNameKey());
        this.entityModelNames.add(getTariffEntityModel().getEntityNameKey());
        this.entityModelNames.add(getStreetEntityModel().getEntityNameKey());
        this.entityModelNames.add(getDisconnectionReasonEntityModel().getEntityNameKey());

        this.documentModelNames.add(getPaymentEntityModel().getEntityNameKey());
        this.documentModelNames.add(getRenderedServiceEntityModel().getEntityNameKey());
        this.documentModelNames.add(getSubscriptionFeeModel().getEntityNameKey());
        this.documentModelNames.add(getMaterialConsumptionEntityModel().getEntityNameKey());

        this.servicesNames = Arrays.stream(FixedServices.values()).filter(e -> e.getId() != 1).map(FixedServices::getCode).collect(Collectors.toList());

        this.reportNames = Arrays.stream(Reports.values()).map(Reports::getCode).collect(Collectors.toList());
    }

    public BaseModel getCurrentModel() {
        return this.currentModel;
    }

    public void setCurrentModel(String code) {
        if(code == getSubscriptionFeeModel().getEntityNameKey()) {
            this.currentModel = getSubscriptionFeeModel();
            notifyObservers(getSubscriptionFeeModel());
            return;
        }

        EntityModel model = getEntityModel(code);

        if (model != null) {
            this.currentModel = model;
            notifyObservers(model);
        }
    }

    public List<String> getEntityItems() {
        return this.entityModelNames;
    }

    public List<String> getDocumentItems() {
        return this.documentModelNames;
    }

    public List<String> getServicesItems() {
        return this.servicesNames;
    }

    public List<String> getReportsItems() {
        return this.reportNames;
    }
}
