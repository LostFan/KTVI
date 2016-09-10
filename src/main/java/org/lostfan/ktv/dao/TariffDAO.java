package org.lostfan.ktv.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;

public interface TariffDAO extends EntityDAO<Tariff> {

    List<Tariff> getTariffsByName(String name);

    TariffPrice getTariffPrice(int tariffId, LocalDate date);

    List<TariffPrice> getTariffPrices(int tariffId);

    List<TariffPrice> getAllTariffPrices();

    void saveTariffPrice(TariffPrice tariffPrice);

    void deleteTariffPrice(TariffPrice tariffPrice);

    BigDecimal getPriceByDate(int tariffId, LocalDate date);
}
