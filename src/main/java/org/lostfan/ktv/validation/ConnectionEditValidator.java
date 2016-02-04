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

        SubscriberSession oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null && !oldSubscriberSession.getConnectionDate().equals(prevRenderedService.getDate())) {
            result.addError("errors.getSessionAfterDate");
            return result;
        }
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberTariff != null && !oldSubscriberTariff.getConnectTariff().equals(prevRenderedService.getDate())) {
            result.addError("errors.getTariffAfterDate");
            return result;
        }

        oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndConnectionDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        if(oldSubscriberSession.getDisconnectionDate() != null) {
            result.addError("errors.alreadySessionClosed");
            return result;
        }
        oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndContainDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        if(oldSubscriberTariff.getDisconnectTariff() != null) {
            result.addError("errors.alreadyTariffClosed");
            return result;
        }

        if(prevRenderedService.getDate().isAfter(entity.getDate())) {
            oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndContainDate(entity.getSubscriberAccount(), entity.getDate());
            if(oldSubscriberSession != null) {
                result.addError("errors.alreadyGetSession");
                return result;
            }
//            oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndContainDate(entity.getSubscriberAccount(), entity.getDate());
//            if(oldSubscriberTariff != null  ) {
//                result.addError("errors.alreadyGetTariff");
//                return result;
//            }
        }

        return result;
    }
}
