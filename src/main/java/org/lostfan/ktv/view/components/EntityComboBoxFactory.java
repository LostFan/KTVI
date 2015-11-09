package org.lostfan.ktv.view.components;

import org.lostfan.ktv.model.*;

public class EntityComboBoxFactory {

    public static EntityComboBox createComboBox(EntityFieldTypes type) {
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
            default:
                throw new IllegalArgumentException("Wrong type: " + type.toString());
        }
        return new EntityComboBox(model);
    }
}
