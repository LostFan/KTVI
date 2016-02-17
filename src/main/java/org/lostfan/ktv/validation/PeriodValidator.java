package org.lostfan.ktv.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PeriodDAO;
import org.lostfan.ktv.domain.Document;
import org.lostfan.ktv.domain.SubscriberTariff;

public class PeriodValidator implements Validator<Document> {

    private PeriodDAO periodDAO;

    public PeriodValidator() {
        this.periodDAO = DAOFactory.getDefaultDAOFactory().getPeriodDAO();
    }

    @Override
    public ValidationResult validate(Document entity, ValidationResult result) {
        LocalDate date = this.periodDAO.getPeriod();
        if (date.isAfter(entity.getDate())) {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("d.MM.yyyy");
            result.addError("errors.periodAlreadyClosed", (Object) date.format(formatter));
            return result;
        }

        return result;
    }

    public ValidationResult validate(LocalDate date, ValidationResult result) {
        LocalDate periodDate = this.periodDAO.getPeriod();
        if (periodDate.isAfter(date)) {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("d.MM.yyyy");
            result.addError("errors.periodAlreadyClosed", (Object) periodDate.format(formatter));
            return result;
        }

        return result;
    }
}
