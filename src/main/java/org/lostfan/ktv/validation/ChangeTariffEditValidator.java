package org.lostfan.ktv.validation;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.SubscriberTariff;

public class ChangeTariffEditValidator implements Validator<RenderedService> {

    private SubscriberDAO subscriberDAO;

    public ChangeTariffEditValidator() {
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public ValidationResult validate(RenderedService entity, ValidationResult result) {
        throw new UnsupportedOperationException("prevRenderedService parameter is required.");
    }

    public ValidationResult validate(RenderedService entity, RenderedService prevRenderedService, ValidationResult result) {

        SubscriberTariff oldSubscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndConnectionDate(prevRenderedService.getSubscriberAccount(), prevRenderedService.getDate());
        if (oldSubscriberTariff.getDisconnectTariff() != null) {
            result.addError("errors.getTariffAfterDate");
            return result;
        }

        if (prevRenderedService.getDate().isAfter(entity.getDate())) {
            SubscriberTariff subscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndContainDate(entity.getSubscriberAccount(), entity.getDate());
            if (subscriberTariff == null) {
                result.addError("errors.noCurrentTariff");
                return result;
            }
            if (!prevRenderedService.getSubscriberAccount().equals(entity.getSubscriberAccount())) {
                return  result;
            }

            if(!prevRenderedService.getDate().equals(subscriberTariff.getDisconnectTariff())) {
                result.addError("errors.getOlderTariffByDate");
                return result;
            }

            subscriberTariff = subscriberDAO.getSubscriberTariffBySubscriberIdAndConnectionDate(entity.getSubscriberAccount(), entity.getDate());
            if(subscriberTariff != null && entity.getDate().equals(subscriberTariff.getConnectTariff())) {
                result.addError("errors.getOlderTariffByDate");
                return result;
            }
        }

        return result;
    }
}
