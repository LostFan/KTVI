package org.lostfan.ktv.dao.impl.hsqldb;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.TariffDAO;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.TestHsqldbConnectionManager;

import static org.junit.Assert.assertEquals;
import static org.lostfan.ktv.utils.DatabaseUtils.executeQuery;
import static org.lostfan.ktv.utils.DatabaseUtils.executeSqlFile;

/**
 * Created by Ihar_Niakhlebau on 23-Sep-15.
 */
public class HsqldbTariffDAOTest {

    private static TariffDAO tariffDAO;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        ConnectionManager.setManager(new TestHsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());
        tariffDAO = DAOFactory.getDefaultDAOFactory().getTariffDAO();
        executeSqlFile("/hsqldb/drop.sql");
        executeSqlFile("/hsqldb/create.sql");
    }

    @Before
    public void clearTable() throws SQLException {
        executeQuery("DELETE FROM \"tariff_price\"");
        executeQuery("DELETE FROM \"tariff\"");
        executeQuery("DELETE FROM \"subscriber\"");
        executeQuery("ALTER TABLE \"tariff\" ALTER COLUMN \"id\" RESTART WITH 1");
    }

    @Test
    public void emptyTariffsTest() {
        assertEquals(tariffDAO.getAll().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectTariffCountTest() throws SQLException {
        insertStubDataTariffs();
        assertEquals(tariffDAO.getAll().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingTariffsTest() throws SQLException {
        insertStubDataTariffs();
        assertEquals(tariffDAO.getAll().get(0).getId().intValue(), 1);
    }

    @Test
    public void getExistingTariffByIdTest() throws SQLException {
        insertStubDataTariffs();
        assertEquals(tariffDAO.get(1).getId().intValue(), 1);
    }

    @Test
    public void createNewTariffCorrectServiceCountTest() throws SQLException {
        insertStubDataTariffs();
        Tariff tariff = new Tariff();
        tariff.setChannels("30");
        tariff.setName("New");
        tariffDAO.save(tariff);
        assertEquals(tariffDAO.getAll().size(), 4);
    }

    @Test
    public void createNewTariffShouldMatchInsertedValuesTest() throws SQLException {
        insertStubDataTariffs();
        Tariff tariff = new Tariff();
        tariff.setChannels("30");
        tariff.setName("New");
        tariffDAO.save(tariff);
        assertEquals(tariffDAO.getAll().get(3).getChannels(), "30");
        assertEquals(tariffDAO.getAll().get(3).getName(), "New");
    }

    @Test
    public void createNewTariffShouldHaveIncrementedIdTest() throws SQLException {
        insertStubDataTariffs();
        Tariff tariff = new Tariff();
        tariff.setChannels("30");
        tariff.setName("New");
        tariffDAO.save(tariff);
        assertEquals(tariffDAO.getAll().get(3).getId().intValue(), 4);
    }

    @Test
    public void updateExistingTariffByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubDataTariffs();
        Tariff tariff = tariffDAO.get(1);
        tariff.setChannels("21");
        tariffDAO.update(tariff);
        assertEquals(tariffDAO.get(1).getChannels(), "21");
    }

    @Test
    public void updateExistingTariffByIdCorrectServiceCountTest() throws SQLException {
        insertStubDataTariffs();
        Tariff tariff = tariffDAO.get(1);
        tariff.setChannels("21");
        tariffDAO.update(tariff);
        assertEquals(tariffDAO.getAll().size(), 3);
    }

    @Test
    public void updateExistingTariffByIdShouldMatchNotUpdatedValuesTest() throws SQLException {
        insertStubDataTariffs();
        Tariff tariff = tariffDAO.get(1);
        tariff.setChannels("21");
        tariffDAO.update(tariff);
        assertEquals(tariffDAO.get(1).getName(), "Uno");
    }

    @Test
    public void deleteTariffByIdCorrectServiceCountTest() throws SQLException {
        insertStubDataTariffs();
        tariffDAO.delete(1);
        assertEquals(tariffDAO.getAll().size(), 2);
    }

    @Test
    public void deleteTariffByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubDataTariffs();
        tariffDAO.delete(1);
        assertEquals(tariffDAO.get(1), null);
    }

    @Test
    public void emptyTariffPricesTest() {
        assertEquals(tariffDAO.getAllTariffPrices().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectTariffPriceCountTest() throws SQLException {
        insertStubData();
        assertEquals(tariffDAO.getAllTariffPrices().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingTariffPricesTest() throws SQLException {
        insertStubData();
        assertEquals(tariffDAO.getAllTariffPrices().get(0).getTariffId().intValue(), 1);
    }

    @Test
    public void getExistingTariffPriceByTariffIdAndDateTest() throws SQLException {
        insertStubData();
        assertEquals(tariffDAO.getTariffPrice(1,LocalDate.of(2015,1,1)).getPrice(), 40000);
    }

    @Test
    public void createNewTariffPriceCorrectServiceCountTest() throws SQLException {
        insertStubData();
        TariffPrice tariffPrice = new TariffPrice();
        tariffPrice.setTariffId(1);
        tariffPrice.setDate(LocalDate.of(2015,10,1));
        tariffPrice.setPrice(15000);
        tariffDAO.saveTariffPrice(tariffPrice);
        assertEquals(tariffDAO.getAllTariffPrices().size(), 4);
    }

    @Test
    public void createNewTariffPriceShouldMatchInsertedValuesTest() throws SQLException {
        insertStubData();
        TariffPrice tariffPrice = new TariffPrice();
        tariffPrice.setTariffId(1);
        tariffPrice.setDate(LocalDate.of(2015, 10, 1));
        tariffPrice.setPrice(15000);
        tariffDAO.saveTariffPrice(tariffPrice);
        assertEquals(tariffDAO.getTariffPrice(1, LocalDate.of(2015, 10, 1)).getPrice(), 15000);
    }

    @Test
    public void deleteTariffPriceByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        tariffDAO.deleteTariffPrice(1, LocalDate.of(2015, 1, 1));
        assertEquals(tariffDAO.getAllTariffPrices().size(), 2);
    }

    @Test
    public void deleteTariffPriceByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubData();
        tariffDAO.deleteTariffPrice(1, LocalDate.of(2015, 1, 1));
        assertEquals(tariffDAO.getTariffPrice(1, LocalDate.of(2015, 1, 1)), null);
    }

    @Test
    public void getTariffPriceIntByTariffIdAndDateTest() throws SQLException {
        insertStubData();
        assertEquals(tariffDAO.getPriceByDate(1, LocalDate.of(2015, 1, 1)).intValue(), 40000);
        assertEquals(tariffDAO.getPriceByDate(1, LocalDate.of(2015, 4, 1)).intValue(), 50000);
    }

    private void insertStubDataSubscribers() throws SQLException {
        executeQuery("INSERT INTO \"subscriber\" (\"id\", \"name\", \"account\") VALUES(1, 'Arya', 700111);");
        executeQuery("INSERT INTO \"subscriber\" (\"id\", \"name\", \"account\") VALUES(2, 'Edard', 700321);");
        executeQuery("INSERT INTO \"subscriber\" (\"id\", \"name\", \"account\") VALUES(3, 'Jon', 700812);");
    }

    private void insertStubDataTariffs() throws SQLException {
        executeQuery("INSERT INTO \"tariff\" (\"id\", \"name\", \"channels\") VALUES(1, 'Uno', 40);");
        executeQuery("INSERT INTO \"tariff\" (\"id\", \"name\", \"channels\") VALUES(2, 'Dos', 30);");
        executeQuery("INSERT INTO \"tariff\" (\"id\", \"name\", \"channels\") VALUES(3, 'Tres', 60);");
    }

    private void insertStubDataTariffPrices() throws SQLException {
        executeQuery("INSERT INTO \"tariff_price\" (\"tariff_id\", \"price\", \"date\") VALUES(1, 40000, '2015-1-1');");
        executeQuery("INSERT INTO \"tariff_price\" (\"tariff_id\", \"price\", \"date\") VALUES(1, 50000, '2015-3-1');");
        executeQuery("INSERT INTO \"tariff_price\" (\"tariff_id\", \"price\", \"date\") VALUES(2, 10000, '2015-1-1');");
    }

    private void insertStubData() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataTariffs();
        insertStubDataTariffPrices();
    }
}
