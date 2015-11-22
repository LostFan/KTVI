package org.lostfan.ktv.model.entity;

import java.util.HashMap;
import java.util.Map;

public final class EntityModelHolder {

    private  EntityModelHolder() { }

//    private static Map<String, EntityModel> entityModels;

    private static MaterialConsumptionEntityModel materialConsumptionEntityModel;
    private static SubscriberEntityModel subscriberEntityModel;

    static {
        materialConsumptionEntityModel = new MaterialConsumptionEntityModel();
        subscriberEntityModel = new SubscriberEntityModel();
    }

    public static MaterialConsumptionEntityModel getMaterialConsumptionEntityModel() {
        return materialConsumptionEntityModel;
    }

    public static SubscriberEntityModel getSubscriberEntityModel() {
        return subscriberEntityModel;
    }
}
