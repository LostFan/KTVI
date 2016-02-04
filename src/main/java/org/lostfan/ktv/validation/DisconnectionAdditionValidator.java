package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;

public class DisconnectionAdditionValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public DisconnectionAdditionValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        SubscriberSession subscriberSession = subscriberDAO.getNotClosedSubscriberSessionByDate(entity.getSubscriberAccount(), entity.getDate());
        if (subscriberSession == null) {
            result.addError("errors.noCurrentSession");
            return result;
        }

        return result;
    }
}
