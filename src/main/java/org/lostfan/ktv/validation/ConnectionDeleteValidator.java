package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.utils.ResourceBundles;

public class ConnectionDeleteValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public ConnectionDeleteValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {

        SubscriberSession oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null) {
            result.addError(ResourceBundles.getGuiBundle().getString("errors.getSessionAfterDate") + ". Id: " + entity.getId());
            return result;
        }
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberTariff != null ) {
            result.addError(ResourceBundles.getGuiBundle().getString("errors.getTariffAfterDate") + ". Id: " + entity.getId());
            return result;
        }

        oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndConnectionDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession.getDisconnectionDate() != null) {
            result.addError(ResourceBundles.getGuiBundle().getString("errors.alreadySessionClosed") + ". Id: " + entity.getId());
            return result;
        }

        return result;
    }
}
