package org.lostfan.ktv.dao.impl.hsqldb;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
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
        assertEquals(serviceDao.getAllServices().get(0).getId(), 123);
    }

    private void insertStubData() throws SQLException {
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(123, 'Service 1 name', false);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(789, 'Service 2 name', true);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(5678, 'Service 3 name', false);");
    }

    private static void executeQuery(String query) throws SQLException {
        Statement stmt = ConnectionManager.getManager().getConnection().createStatement();
        stmt.execute(query);
    }
}
