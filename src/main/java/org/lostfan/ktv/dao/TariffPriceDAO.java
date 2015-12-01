package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;

public interface TariffPriceDAO extends EntityDAO<TariffPrice> {

    List<TariffPrice> getAll();

    TariffPrice get(int id);

    void save(TariffPrice tariff);

    void update(TariffPrice tariff);

    void delete(int tariffId);

    List<TariffPrice> getAllContainsInName(String str);

    Integer getPriceByDate(int tariffId, LocalDate date);

    TariffPrice getByTariffIdAndDate(int tariffId, LocalDate date);

    void deleteByTariffIdAndDate(int tariffId, LocalDate date);
}
