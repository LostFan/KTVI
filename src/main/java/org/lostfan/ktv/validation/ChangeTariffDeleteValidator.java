package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.utils.ResourceBundles;

public class ChangeTariffDeleteValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public ChangeTariffDeleteValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {

        SubscriberSession oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null) {
            result.addError(ResourceBundles.getGuiBundle().getString("errors.hasSessionAfterDate") + ". Id: " + entity.getId());
            return result;
        }
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberTariff != null ) {
            result.addError(ResourceBundles.getGuiBundle().getString("errors.hasTariffAfterDate") + ". Id: " + entity.getId());
            return result;
        }

        oldSubscriberSession = subscriberDAO.getSubscriberSessionByConnectionDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null && oldSubscriberSession.getDisconnectionDate() != null) {
            result.addError(ResourceBundles.getGuiBundle().getString("errors.sessionAlreadyClosed") + ". Id: " + entity.getId());
            return result;
        }

        return result;
    }
}
