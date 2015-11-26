package org.lostfan.ktv.dao.impl.hsqldb;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.TestHsqldbConnectionManager;

import static org.junit.Assert.assertEquals;
import static org.lostfan.ktv.utils.DatabaseUtils.executeQuery;
import static org.lostfan.ktv.utils.DatabaseUtils.executeSqlFile;

/**
 * Created by Ihar_Niakhlebau on 23-Sep-15.
 */
public class HsqldbRenderedServiceDAOTest {

    private static RenderedServiceDAO renderedServiceDao;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        ConnectionManager.setManager(new TestHsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());
        renderedServiceDao = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
        executeSqlFile("/hsqldb/drop.sql");
        executeSqlFile("/hsqldb/create.sql");
    }

    @Before
    public void clearTable() throws SQLException {
        executeQuery("DELETE FROM \"rendered_service\"");
        executeQuery("DELETE FROM \"service\"");
        executeQuery("DELETE FROM \"subscriber\"");
        executeQuery("ALTER TABLE \"rendered_service\" ALTER COLUMN \"id\" RESTART WITH 1");
    }

    @Test
    public void emptyRenderedServicesTest() {
        assertEquals(renderedServiceDao.getAll().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectRenderedServiceCountTest() throws SQLException {
        insertStubData();
        assertEquals(renderedServiceDao.getAll().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingRenderedServicesTest() throws SQLException {
        insertStubData();
        assertEquals(renderedServiceDao.getAll().get(0).getId().intValue(), 1);
    }

    @Test
    public void getExistingRenderedServiceByIdTest() throws SQLException {
        insertStubData();
        assertEquals(renderedServiceDao.get(1).getId().intValue(), 1);
    }

    @Test
    public void createNewRenderedServiceCorrectServiceCountTest() throws SQLException {
        insertStubData();
        RenderedService renderedService = new RenderedService();
        renderedService.setServiceId(1);
        renderedService.setSubscriberId(3);
        renderedService.setDate(LocalDate.of(2015,11,11));
        renderedServiceDao.save(renderedService);
        assertEquals(renderedServiceDao.getAll().size(), 4);
    }

    @Test
    public void createNewRenderedServiceShouldMatchInsertedValuesTest() throws SQLException {
        insertStubData();
        RenderedService renderedService = new RenderedService();
        renderedService.setServiceId(1);
        renderedService.setSubscriberId(3);
        renderedService.setDate(LocalDate.of(2015, 11, 11));
        renderedServiceDao.save(renderedService);
        assertEquals(renderedServiceDao.getAll().get(3).getServiceId(), 1);
        assertEquals(renderedServiceDao.getAll().get(3).getSubscriberId(), 3);
        assertEquals(renderedServiceDao.getAll().get(3).getDate(), LocalDate.of(2015, 11, 11));
    }

    @Test
    public void createNewRenderedServiceShouldHaveIncrementedIdTest() throws SQLException {
        insertStubData();
        RenderedService renderedService = new RenderedService();
        renderedService.setServiceId(1);
        renderedService.setSubscriberId(3);
        renderedService.setDate(LocalDate.of(2015, 11, 11));
        renderedServiceDao.save(renderedService);
        assertEquals(renderedServiceDao.getAll().get(3).getId().intValue(), 4);
    }

    @Test
    public void updateExistingRenderedServiceByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubData();
        RenderedService renderedService = renderedServiceDao.get(1);
        renderedService.setServiceId(2);
        renderedServiceDao.update(renderedService);
        assertEquals(renderedServiceDao.get(1).getServiceId(), 2);
    }

    @Test
    public void updateExistingRenderedServiceByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        RenderedService renderedService = renderedServiceDao.get(1);
        renderedService.setServiceId(2);
        renderedService.setSubscriberId(2);
        renderedServiceDao.update(renderedService);
        assertEquals(renderedServiceDao.getAll().size(), 3);
    }

    @Test
    public void updateExistingRenderedServiceByIdShouldMatchNotUpdatedValuesTest() throws SQLException {
        insertStubData();
        RenderedService renderedService = renderedServiceDao.get(1);
        renderedService.setServiceId(2);
        renderedServiceDao.update(renderedService);
        assertEquals(renderedServiceDao.get(1).getSubscriberId(), 1);
    }

    @Test
    public void deleteRenderedServiceByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        renderedServiceDao.delete(1);
        assertEquals(renderedServiceDao.getAll().size(), 2);
    }

    @Test
    public void deleteRenderedServiceByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubData();
        renderedServiceDao.delete(1);
        assertEquals(renderedServiceDao.get(1), null);
    }

    @Test
    public void getAllRenderedServicesBySubscriberIdShouldReturnsCorrectCountTest() throws SQLException {
        insertStubData();
        assertEquals(renderedServiceDao.getRenderedServicesBySubscriberId(1).size(), 2);
        assertEquals(renderedServiceDao.getRenderedServicesBySubscriberId(2).size(), 1);
        assertEquals(renderedServiceDao.getRenderedServicesBySubscriberId(3).size(), 0);
    }

    @Test
    public void getAllRenderedServicesBySubscriberIdShouldReturnsCorrectDataTest() throws SQLException {
        insertStubData();
        assertEquals(renderedServiceDao.getRenderedServicesBySubscriberId(1).get(0).getServiceId(), 1);
        assertEquals(renderedServiceDao.getRenderedServicesBySubscriberId(2).get(0).getServiceId(), 3);
    }

    @Test
    public void getAllRenderedServicesByDateShouldReturnsCorrectCountTest() throws SQLException {
        insertStubData();
        assertEquals(renderedServiceDao.getRenderedServicesByDate(LocalDate.of(2015, 8, 3)).size(), 2);
        assertEquals(renderedServiceDao.getRenderedServicesByDate(LocalDate.of(2015, 6, 27)).size(), 1);
        assertEquals(renderedServiceDao.getRenderedServicesByDate(LocalDate.of(2015, 1, 1)).size(), 0);
    }

    @Test
    public void getAllRenderedServicesByDateShouldReturnsCorrectDataTest() throws SQLException {
        insertStubData();
        assertEquals(renderedServiceDao.getRenderedServicesByDate(LocalDate.of(2015, 6, 27)).get(0).getServiceId(), 2);
        assertEquals(renderedServiceDao.getRenderedServicesByDate(LocalDate.of(2015, 6, 27)).get(0).getSubscriberId(), 1);
    }

    private void insertStubDataServices() throws SQLException {
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(1, 'Service 1 name', false);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(2, 'Service 2 name', true);");
        executeQuery("INSERT INTO \"service\" (\"id\", \"name\", \"additional\") VALUES(3, 'Service 3 name', false);");
    }

    private void insertStubDataSubscribers() throws SQLException {
        executeQuery("INSERT INTO \"subscriber\" (\"id\", \"name\", \"account\") VALUES(1, 'Arya', 700111);");
        executeQuery("INSERT INTO \"subscriber\" (\"id\", \"name\", \"account\") VALUES(2, 'Edard', 700321);");
        executeQuery("INSERT INTO \"subscriber\" (\"id\", \"name\", \"account\") VALUES(3, 'Jon', 700812);");
    }

    private void insertStubDataRenderedServices() throws SQLException {
        executeQuery("INSERT INTO \"rendered_service\" (\"id\", \"subscriber_id\", \"service_id\", \"date\") VALUES(1, 1, 1, '2015-8-3');");
        executeQuery("INSERT INTO \"rendered_service\" (\"id\", \"subscriber_id\", \"service_id\", \"date\") VALUES(2, 1, 2, '2015-6-27');");
        executeQuery("INSERT INTO \"rendered_service\" (\"id\", \"subscriber_id\", \"service_id\", \"date\") VALUES(3, 2, 3, '2015-8-3');");
    }

    private void insertStubData() throws SQLException{
        insertStubDataServices();
        insertStubDataSubscribers();
        insertStubDataRenderedServices();
    }
}
