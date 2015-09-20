package org.lostfan.ktv.dao.impl.hsqldb;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.TestHsqldbConnectionManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class HsqldbServiceDAOTest {

    private static ServiceDAO serviceDao;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        ConnectionManager.setManager(new TestHsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());
        serviceDao = DAOFactory.getDefaultDAOFactory().getServiceDAO();

        InputStream is = HsqldbServiceDAOTest.class.getResourceAsStream("/hsqldb/create.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        StringBuilder queryBuild = new StringBuilder();

        String line = reader.readLine();
        while (line != null) {
            queryBuild.append(line);
            line = reader.readLine();
        }
        executeQuery(queryBuild.toString());
    }

    @Before
    public void clearTable() throws SQLException {
        executeQuery("DELETE FROM \"service_price\"");
        executeQuery("DELETE FROM \"service\"");
    }

    @Test
    public void emptyServicesTest() {
        assertEquals(serviceDao.getAllServices().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectServiceCountTest() throws SQLException {
        insertStubDataServices();
        assertEquals(serviceDao.getAllServices().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingServicesTest() throws SQLException {
        insertStubDataServices();
        assertEquals(serviceDao.getAllServices().get(0).getId(), 1);
    }

    @Test
    public void getExistingServiceByIdTest() throws SQLException {
        insertStubDataServices();
        assertEquals(serviceDao.getService(1).getId(), 1);
    }

    @Test
    public void createNewServiceCorrectServiceCountTest() throws SQLException {
        insertStubDataServices();
        Service service = new Service();
        service.setName("Service 4 name");
        service.setAdditionalService(true);
        serviceDao.save(service);
        assertEquals(serviceDao.getAllServices().size(), 4);
    }

    @Test
    public void createNewServiceShouldMatchInsertedValuesTest() throws SQLException {
        insertStubDataServices();
        Service service = new Service();
        service.setName("Service 4 name");
        service.setAdditionalService(true);
        serviceDao.save(service);
        assertEquals(serviceDao.getAllServices().get(3).getName(), "Service 4 name");
        assertEquals(serviceDao.getAllServices().get(3).isAdditionalService(), true);
    }

    @Test
    public void createNewServiceShouldHaveIncrementedIdTest() throws SQLException {
        insertStubDataServices();
        Service service = new Service();
        service.setName("Service 4 name");
        service.setAdditionalService(true);
        serviceDao.save(service);
        assertEquals(serviceDao.getAllServices().get(3).getId(), 4);
    }

    @Test
    public void updateExistingServiceByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubDataServices();
        Service service = serviceDao.getService(1);
        service.setName("Service 1 name new");
        serviceDao.update(service);
        assertEquals(serviceDao.getService(1).getName(), "Service 1 name new");
    }

    @Test
    public void updateExistingServiceByIdCorrectServiceCountTest() throws SQLException {
        insertStubDataServices();
        Service service = serviceDao.getService(1);
        service.setAdditionalService(false);
        service.setName("Service 1 name new");
        serviceDao.update(service);
        assertEquals(serviceDao.getAllServices().size(), 3);
    }

    @Test
    public void updateExistingServiceByIdShouldMatchNotUpdatedValuesTest() throws SQLException {
        insertStubDataServices();
        Service service = serviceDao.getService(1);
        service.setAdditionalService(false);
        serviceDao.update(service);
        assertEquals(serviceDao.getService(1).getName(), "Service 1 name");
    }

    @Test
    public void deleteServiceByIdCorrectServiceCountTest() throws SQLException {
        insertStubDataServices();
        serviceDao.delete(1);
        assertEquals(serviceDao.getAllServices().size(), 2);
    }

    @Test
    public void deleteServiceByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubDataServices();
        serviceDao.delete(1);
        assertEquals(serviceDao.getService(1), null);
    }

    @Test
    public void getServicePriceByIdAndDateShouldGetCorrectDataTest() throws SQLException {
        insertStubDataServices();
        insertStubDataServicePrices();
        assertEquals(serviceDao.getPriceByDay(1, LocalDate.of(2015, 3, 1)), 40000);
        assertEquals(serviceDao.getPriceByDay(1, LocalDate.of(2015, 4, 2)), 50000);
        assertEquals(serviceDao.getPriceByDay(1, LocalDate.of(2015, 6, 1)), 50000);
    }

    @Test
    public void getAllServicePriceByIdAndDateShouldReturnsCorrectServiceCountTest() throws SQLException {
        insertStubDataServices();
        insertStubDataServicePrices();
        assertEquals(serviceDao.getServicePricesByServiceId(1).size(), 2);
        assertEquals(serviceDao.getServicePricesByServiceId(2).size(), 1);
    }

    private void insertStubDataServices() throws SQLException {
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(1, 'Service 1 name', false);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(2, 'Service 2 name', true);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(3, 'Service 3 name', false);");
    }

    private void insertStubDataServicePrices() throws SQLException {
        executeQuery("INSERT INTO \"service_price\" (\"service_id\", \"date\", \"price\") VALUES(1, '2015-01-01', 40000);");
        executeQuery("INSERT INTO \"service_price\" (\"service_id\", \"date\", \"price\") VALUES(1, '2015-04-02', 50000);");
        executeQuery("INSERT INTO \"service_price\" (\"service_id\", \"date\", \"price\") VALUES(2, '2015-03-02', 10000);");
    }


    private static void executeQuery(String query) throws SQLException {
        Statement stmt = ConnectionManager.getManager().getConnection().createStatement();
        stmt.execute(query);
    }
}
