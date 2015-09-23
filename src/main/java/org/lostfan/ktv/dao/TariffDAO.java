package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Tariff;

public interface TariffDAO {

    List<Tariff> getAllTariffs();

    Tariff getTariff(String name);

    void save(Tariff tariff);

    void update(Tariff tariff);

    void delete(Tariff tariff);

    int getPriceByDate(Tariff tariff, LocalDate date);
}
