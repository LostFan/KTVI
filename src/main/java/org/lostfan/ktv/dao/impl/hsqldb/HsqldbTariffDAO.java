package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.TariffDAO;
import org.lostfan.ktv.domain.Tariff;

import java.time.LocalDate;
import java.util.List;

public class HsqldbTariffDAO implements TariffDAO {

    public List<Tariff> getAllTariffs() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Tariff getTariff(String name) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void save(Tariff tariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void update(Tariff tariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void delete(Tariff tariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getPriceByDate(Tariff tariff, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
