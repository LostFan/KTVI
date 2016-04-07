package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;

public class ConnectionEditValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public ConnectionEditValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        throw new UnsupportedOperationException("prevRenderedService parameter is required.");
    }

    public ValidationResult validate(RenderedService entity, RenderedService prevRenderedService, ValidationResult result) {

        SubscriberSession oldSubscriberSession = subscriberDAO.getSubscriberSessionAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null && !oldSubscriberSession.getConnectionDate().equals(prevRenderedService.getDate())) {
            result.addError("errors.hasSessionAfterDate");
            return result;
        }
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberTariff != null && !oldSubscriberTariff.getConnectTariff().equals(prevRenderedService.getDate())) {
            result.addError("errors.hasTariffAfterDate");
            return result;
        }

        oldSubscriberSession = subscriberDAO.getSubscriberSessionByConnectionDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        if(oldSubscriberSession.getDisconnectionDate() != null) {
            result.addError("errors.sessionAlreadyClosed");
            return result;
        }
        oldSubscriberTariff = subscriberDAO.getSubscriberTariffAtDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        if(oldSubscriberTariff.getDisconnectTariff() != null) {
            result.addError("errors.tariffAlreadyClosed");
            return result;
        }

        if(prevRenderedService.getDate().isAfter(entity.getDate())) {
            oldSubscriberSession = subscriberDAO.getSubscriberSessionAtDate(entity.getSubscriberAccount(), entity.getDate());
            if(oldSubscriberSession != null) {
                result.addError("errors.alreadyGotSession");
                return result;
            }
        }

        return result;
    }
}
