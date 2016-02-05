package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;

public class DisconnectionEditValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public DisconnectionEditValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        throw new UnsupportedOperationException("prevRenderedService parameter is required.");
    }

    public ValidationResult validate(RenderedService entity, RenderedService prevRenderedService, ValidationResult result) {

        SubscriberSession subscriberSessionAfter = subscriberDAO.getSubscriberSessionAfterDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        if (subscriberSessionAfter != null) {
            result.addError("errors.hasSessionAfterDate");
            return result;
        }
        if (prevRenderedService.getDate().isAfter(entity.getDate())) {
            SubscriberSession subscriberSession = subscriberDAO.getSubscriberSessionAtDate(entity.getSubscriberAccount(), entity.getDate());
            if (subscriberSession == null || subscriberSession.getDisconnectionDate() != null) {
                result.addError("errors.noCurrentSession");
                return result;
            }
        }

        return result;
    }
}
