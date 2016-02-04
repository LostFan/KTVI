package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;

public class ChangeTariffAdditionValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public ChangeTariffAdditionValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        SubscriberTariff subscriberTariff = subscriberDAO.getNotClosedSubscriberTariffByDate(entity.getSubscriberAccount(), entity.getDate());
        if (subscriberTariff == null) {
            result.addError("errors.noCurrentTariff");
            return result;
        }

        return result;
    }
}
