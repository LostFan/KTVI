package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.utils.ResourceBundles;

public class DisconnectionDeleteValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public DisconnectionDeleteValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {

        SubscriberSession oldSubscriberSession = subscriberDAO.getSubscriberSessionAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null) {
            result.addError("errors.hasSessionAfterDateWithId", entity.getId());
            return result;
        }
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberTariff != null ) {
            result.addError("errors.hasTariffAfterDateWithId", entity.getId());
            return result;
        }

        return result;
    }
}
