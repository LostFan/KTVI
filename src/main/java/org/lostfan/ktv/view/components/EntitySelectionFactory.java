package org.lostfan.ktv.view.components;

import org.lostfan.ktv.model.entity.BaseEntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.MaterialEntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.model.entity.ServiceEntityModel;
import org.lostfan.ktv.model.entity.StreetEntityModel;
import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.model.entity.TariffEntityModel;
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
        EntitySearcherModel model = null;
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

}
