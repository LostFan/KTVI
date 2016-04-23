package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;

public class DisconnectionAdditionValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public DisconnectionAdditionValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        SubscriberSession subscriberSession = subscriberDAO.getNotClosedSubscriberSession(entity.getSubscriberAccount(), entity.getDate());
        if (subscriberSession == null) {
            result.addError("errors.noCurrentSession");
            return result;
        }
        if (subscriberSession.getConnectionDate().equals(entity.getDate())) {
            result.addError("errors.disconnectionInOneDayWithConnection");
            return result;
        }

        return result;
    }
}
