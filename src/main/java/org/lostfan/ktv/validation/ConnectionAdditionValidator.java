package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;

import java.util.List;

public class ConnectionAdditionValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public ConnectionAdditionValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {

        List<SubscriberSession> subscriberSessions  = subscriberDAO.getSubscriberSessions(entity.getSubscriberAccount());
        if(!subscriberSessions.isEmpty()) {
            result.addError("errors.alreadyGotConnection");
            result.addError("info.reconnection");
            return result;
        }

        SubscriberSession oldSubscriberSession = subscriberDAO.getSubscriberSessionAtDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null) {
            result.addError("errors.alreadyGotSession");
            return result;
        }
        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffAtDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberTariff != null) {
            result.addError("errors.alreadyGetTariff");
            return result;
        }

        oldSubscriberSession = subscriberDAO.getSubscriberSessionAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null) {
            result.addError("errors.hasSessionAfterDate");
            return result;
        }
        return result;
    }
}
