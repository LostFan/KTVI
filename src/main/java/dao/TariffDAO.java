package dao;

import java.time.LocalDate;
import java.util.List;

import domain.Tariff;

public interface TariffDAO {

    public List<Tariff> getAllTariffs();

    public Tariff getTariff(String name);

    public void save(Tariff tariff);

    public void update(Tariff tariff);

    public void delete(Tariff tariff);

    public int getCostByDay(Tariff tariff, LocalDate date);
}
