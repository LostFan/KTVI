package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberSession;

public class ConnectionAdditionValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public ConnectionAdditionValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        SubscriberSession oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndContainDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null) {
            result.addError("errors.alreadyGotSession");
            return result;
        }
//        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndContainDate(entity.getSubscriberAccount(), entity.getDate());
//        if(oldSubscriberTariff != null) {
//            result.addError("errors.alreadyGetTariff");
//            return result;
//        }
        oldSubscriberSession = subscriberDAO.getSubscriberSessionBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
        if(oldSubscriberSession != null) {
            result.addError("errors.hasSessionAfterDate");
            return result;
        }
//        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndAfterDate(entity.getSubscriberAccount(), entity.getDate());
//        if(oldSubscriberTariff != null) {
//            result.addError("errors.getTariffAfterDate");
//            return result;
//        }
        return result;
    }
}
