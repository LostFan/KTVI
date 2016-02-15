package org.lostfan.ktv.dao;

import java.time.LocalDate;

public interface PeriodDAO {

    void savePeriod(LocalDate date);

    LocalDate getPeriod();
}
