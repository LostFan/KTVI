package org.lostfan.ktv.model;

import java.time.LocalDate;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PeriodDAO;

public class PeriodModel {

    private PeriodDAO periodDAO = DAOFactory.getDefaultDAOFactory().getPeriodDAO();

    public String getName() {
        return "menu.file.period";
    }

    public LocalDate getPeriod() {
        return periodDAO.getPeriod();
    }

    public void savePeriod(LocalDate date) {
        periodDAO.savePeriod(date);
    }
}
