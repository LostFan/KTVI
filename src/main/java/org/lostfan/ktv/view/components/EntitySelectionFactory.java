package org.lostfan.ktv.view.components;

import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.searcher.AdditionalServiceSearcherModel;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.model.searcher.MaterialSearcherModel;
import org.lostfan.ktv.model.searcher.RenderedServiceSearcherModel;
import org.lostfan.ktv.model.searcher.ServiceSearcherModel;
import org.lostfan.ktv.model.searcher.StreetSearcherModel;
import org.lostfan.ktv.model.searcher.SubscriberSearcherModel;
import org.lostfan.ktv.model.searcher.TariffSearcherModel;
import org.lostfan.ktv.view.EntitySelectionView;

public class EntitySelectionFactory {

    public static EntitySelectionView createForm(EntityFieldTypes type) {
        EntitySearcherModel model;
        switch (type) {
            case Street:
                model = new StreetSearcherModel();
                break;
            case Service:
                model = new ServiceSearcherModel();
                break;
            case Subscriber:
                model = new SubscriberSearcherModel();
                break;
            case Material:
                model = new MaterialSearcherModel();
                break;
            case RenderedService:
                model = new RenderedServiceSearcherModel();
                break;
            case Tariff:
                model = new TariffSearcherModel();
                break;
            default:
                throw new IllegalArgumentException("Wrong type: " + type.toString());
        }
        return new EntitySelectionView(model);
    }

    public static EntitySelectionView createAdditionalServiceForm() {
        return new EntitySelectionView(new AdditionalServiceSearcherModel());
    }

    public static EntitySelectionView createView(String entityKey) {
        EntityModel entityModel = MainModel.getEntityModel(entityKey);
        if (entityModel == null) {
            throw new IllegalArgumentException("Unknown entity name: " + entityKey);
        }

        EntitySearcherModel searcherModel = entityModel.createSearchModel();
        if (searcherModel == null) {
            throw new IllegalArgumentException("Entity for " + entityKey + " does not support EntitySearchModel");
        }

        return new EntitySelectionView(searcherModel);
    }
}
