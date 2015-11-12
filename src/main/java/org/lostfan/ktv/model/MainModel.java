package org.lostfan.ktv.model;

import org.lostfan.ktv.model.entity.*;
import org.lostfan.ktv.utils.BaseObservable;

import java.util.*;

public class MainModel extends BaseObservable {

    private Map<String, EntityModel> entityModels;
    private Map<String, EntityModel> documentModels;
    private EntityModel currentModel;

    public MainModel() {
        this.entityModels = new LinkedHashMap<>();
        this.documentModels = new LinkedHashMap<>();

        EntityModel model = new ServiceEntityModel();
        this.entityModels.put(model.getEntityNameKey(), model);
        model = new ServiceEntityModel();
        this.entityModels.put(model.getEntityNameKey(), model);
        model = new SubscriberEntityModel();
        this.entityModels.put(model.getEntityNameKey(), model);
        model = new MaterialEntityModel();
        this.entityModels.put(model.getEntityNameKey(), model);
        model = new TariffEntityModel();
        this.entityModels.put(model.getEntityNameKey(), model);
        model = new StreetEntityModel();
        this.entityModels.put(model.getEntityNameKey(), model);

        model = new PaymentEntityModel();
        this.documentModels.put(model.getEntityNameKey(), model);
        model = new RenderedServiceEntityModel();
        this.documentModels.put(model.getEntityNameKey(), model);
        model = new MaterialConsumptionEntityModel();
        this.documentModels.put(model.getEntityNameKey(), model);
    }

    public EntityModel getCurrentModel() {
        return this.currentModel;
    }

    public void setCurrentModel(String code) {
        EntityModel model = this.entityModels.get(code);
        if (model == null) {
            model = this.documentModels.get(code);
        }

        if (model != null) {
            this.currentModel = model;
            notifyObservers(model);
        }
    }

    public Set<String> getEntityItems() {
        return this.entityModels.keySet();
    }

    public Set<String> getDocumentItems() {
        return this.documentModels.keySet();
    }
}
