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
        executeQuery("DELETE FROM \"service\"");
    }

    @Test
    public void emptyServicesTest() {
        assertEquals(serviceDao.getAllServices().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectServiceCountTest() throws SQLException {
        insertStubData();
        assertEquals(serviceDao.getAllServices().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingServicesTest() throws SQLException {
        insertStubData();
        assertEquals(serviceDao.getAllServices().get(0).getId(), 1);
    }

    @Test
    public void getExistingServiceByIdTest() throws SQLException {
        insertStubData();
        assertEquals(serviceDao.getService(1).getId(), 1);
    }

    @Test
    public void createNewServiceTest() throws SQLException {
        insertStubData();
        Service service = new Service();
        service.setName("Service 4 name");
        service.setAdditionalService(true);
        serviceDao.save(service);
        assertEquals(serviceDao.getAllServices().size(), 4);
        assertEquals(serviceDao.getAllServices().get(3).getName(), "Service 4 name");
        assertEquals(serviceDao.getAllServices().get(3).getId(), 4);
    }

    @Test
    public void updateExistingServiceByIdTest() throws SQLException {
        insertStubData();
        Service service = serviceDao.getService(1);
        service.setName("Service 1 name new");
        serviceDao.update(service);
        assertEquals(serviceDao.getService(1).getName(), "Service 1 name new");
    }

    @Test
    public void deleteServiceByIdTest() throws SQLException {
        insertStubData();
        serviceDao.delete(1);
        assertEquals(serviceDao.getAllServices().size(), 2);
    }

    private void insertStubData() throws SQLException {
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(1, 'Service 1 name', false);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(2, 'Service 2 name', true);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(3, 'Service 3 name', false);");

    }

    private static void executeQuery(String query) throws SQLException {
        Statement stmt = ConnectionManager.getManager().getConnection().createStatement();
        stmt.execute(query);
    }
}
