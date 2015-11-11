package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;

public interface TariffDAO extends EntityDAO<Tariff> {

    List<Tariff> getAll();

    Tariff get(int id);

    List<Tariff> getTariffsByName(String name);

    void save(Tariff tariff);

    void update(Tariff tariff);

    void delete(int tariffId);

    List<Tariff> getAllContainsInName(String str);

    TariffPrice getTariffPrice(int tariffId, LocalDate date);

    List<TariffPrice> getAllTariffPrices();

    void saveTariffPrice(TariffPrice tariffPrice);

    void deleteTariffPrice(int tariffId, LocalDate date);

    Integer getPriceByDate(int tariffId, LocalDate date);
}
