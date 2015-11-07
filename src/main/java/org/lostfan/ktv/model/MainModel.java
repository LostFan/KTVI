package org.lostfan.ktv.model;

import org.lostfan.ktv.utils.Observable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainModel extends Observable {

    private List<EntityModel> entityModels;
    private List<EntityModel> documentModels;
    private JPanel contentPanel;

    public MainModel() {
        this.entityModels = new ArrayList<>();
        this.documentModels = new ArrayList<>();
        this.entityModels.add(new ServiceEntityModel());
        this.entityModels.add(new SubscriberEntityModel());
        this.entityModels.add(new MaterialEntityModel());
        this.entityModels.add(new TariffEntityModel());
        this.entityModels.add(new StreetEntityModel());
        this.documentModels.add(new PaymentEntityModel());
        this.documentModels.add(new MaterialConsumptionEntityModel());
    }

    public JPanel getContentPanel() {
        return this.contentPanel;
    }

    public void setContentPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        this.notifyObservers(null);
    }

    public List<EntityModel> getEntityModels() {
        return this.entityModels;
    }

    public List<EntityModel> getDocumentModels() {
        return this.documentModels;
    }
}
