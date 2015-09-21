package org.lostfan.ktv.dao.impl.hsqldb;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.TestHsqldbConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HsqldbSubscriberDAOTest {

    private static SubscriberDAO subscriberDao;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        ConnectionManager.setManager(new TestHsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());
        subscriberDao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();

        InputStream is = HsqldbSubscriberDAOTest.class.getResourceAsStream("/hsqldb/create.sql");
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
        executeQuery("DELETE FROM \"subscriber_session\"");
        executeQuery("DELETE FROM \"subscriber_tariff\"");
        executeQuery("DELETE FROM \"material\"");
        executeQuery("DELETE FROM \"tariff\"");
        executeQuery("DELETE FROM \"subscriber\"");
    }

    @Test
    public void emptySubscribersTest() {
        assertEquals(subscriberDao.getAllSubscribers().size(), 0);
    }

    @Test
    public void getAllSubscribersReturnsCorrectSubscriberCountTest() throws SQLException {
        insertStubDataSubscribers();
        assertEquals(subscriberDao.getAllSubscribers().size(), 3);
    }

    @Test
    public void getAllSubscribersReturnsAllExistingSubscribersTest() throws SQLException {
        insertStubDataSubscribers();
        assertEquals(subscriberDao.getAllSubscribers().get(0).getId(), 1);
    }

    @Test
    public void getExistingSubscriberByIdTest() throws SQLException {
        insertStubDataSubscribers();
        assertEquals(subscriberDao.getSubscriber(1).getId(), 1);
    }

    @Test
    public void createNewSubscriberAndShouldHaveIncrementedIdTest() throws SQLException {
        insertStubDataSubscribers();
        Subscriber subscriber = new Subscriber();
        subscriber.setName("Jayme");
        subscriber.setAccount(700500);
        subscriberDao.save(subscriber);
        assertEquals(subscriberDao.getAllSubscribers().get(3).getId(), 4);
    }

    @Test
    public void createNewSubscriberCorrectSubscriberCountTest() throws SQLException {
        insertStubDataSubscribers();
        Subscriber subscriber = new Subscriber();
        subscriber.setName("Robb");
        subscriber.setAccount(700453);
        subscriberDao.save(subscriber);
        assertEquals(subscriberDao.getAllSubscribers().size(), 4);
    }

    @Test
    public void createNewSubscriberShouldMatchInsertedValuesTest() throws SQLException {
        insertStubDataSubscribers();
        Subscriber subscriber = new Subscriber();
        subscriber.setName("Sansa");
        subscriber.setAccount(700989);
        subscriberDao.save(subscriber);
        assertEquals(subscriberDao.getAllSubscribers().get(3).getName(), "Sansa");
        assertEquals(subscriberDao.getAllSubscribers().get(3).getAccount(), 700989);
    }

    @Test
    public void updateExistingSubscriberByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubDataSubscribers();
        Subscriber subscriber = subscriberDao.getSubscriber(1);
        subscriber.setName("No one");
        subscriberDao.update(subscriber);
        assertEquals(subscriberDao.getSubscriber(1).getName(), "No one");
    }

    @Test
    public void updateExistingSubscriberByIdCorrectSubscriberCountTest() throws SQLException {
        insertStubDataSubscribers();
        Subscriber subscriber = subscriberDao.getSubscriber(1);
        subscriber.setAccount(700736);
        subscriber.setName("No one");
        subscriberDao.update(subscriber);
        assertEquals(subscriberDao.getAllSubscribers().size(), 3);
    }

    @Test
    public void updateExistingSubscriberByIdShouldMatchNotUpdatedValuesTest() throws SQLException {
        insertStubDataSubscribers();
        Subscriber subscriber = subscriberDao.getSubscriber(1);
        subscriber.setAccount(700344);
        subscriberDao.update(subscriber);
        assertEquals(subscriberDao.getSubscriber(1).getName(), "Arya");
    }

    @Test
    public void deleteSubscriberByIdCorrectSubscriberCountTest() throws SQLException {
        insertStubDataSubscribers();
        subscriberDao.delete(1);
        assertEquals(subscriberDao.getAllSubscribers().size(), 2);
    }

    @Test
    public void deleteSubscriberByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubDataSubscribers();
        subscriberDao.delete(1);
        assertEquals(subscriberDao.getSubscriber(1), null);
    }

    @Test
    public void getSubscriberTariffByIdAndDateShouldGetCorrectDataTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataTariffs();
        assertEquals(subscriberDao.getTariffIdByDate(1, LocalDate.of(2015, 4, 2)).intValue(), 1);
        assertEquals(subscriberDao.getTariffIdByDate(1, LocalDate.of(2015, 7, 27)).intValue(), 1);
        assertEquals(subscriberDao.getTariffIdByDate(1, LocalDate.of(2015, 9, 3)).intValue(), 2);
    }

    @Test
    public void getSubscriberTariffByIdAndDateShouldGetNullCorrectDataTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataTariffs();
        assertEquals(subscriberDao.getTariffIdByDate(1, LocalDate.of(2015, 3, 1)), null);
        assertEquals(subscriberDao.getTariffIdByDate(1, LocalDate.of(2015, 7, 28)), null);
        assertEquals(subscriberDao.getTariffIdByDate(1, LocalDate.of(2015, 8, 1)), null);
    }

    @Test
    public void getSubscriberSessionByIdAndDateShouldGetCorrectDataTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataSessions();
        assertEquals(subscriberDao.getSessionIdByDate(1, LocalDate.of(2015, 4, 2)).intValue(), 1);
        assertEquals(subscriberDao.getSessionIdByDate(1, LocalDate.of(2015, 7, 27)).intValue(), 1);
        assertEquals(subscriberDao.getSessionIdByDate(1, LocalDate.of(2015, 9, 3)).intValue(), 2);
    }

    @Test
    public void getSubscriberSessionByIdAndDateShouldGetNullCorrectDataTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataSessions();
        assertEquals(subscriberDao.getSessionIdByDate(1, LocalDate.of(2015, 3, 1)), null);
        assertEquals(subscriberDao.getSessionIdByDate(1, LocalDate.of(2015, 7, 28)), null);
        assertEquals(subscriberDao.getSessionIdByDate(1, LocalDate.of(2015, 8, 1)), null);
    }

    @Test
    public void getAllSubscriberSessionsByIdShouldReturnsCorrectServiceCountTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataSessions();
        assertEquals(subscriberDao.getSubscriberSessions(1).size(), 2);
        assertEquals(subscriberDao.getSubscriberSessions(2).size(), 0);
    }

    @Test
    public void getAllSubscriberSessionsByIdShouldReturnsCorrectDataTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataSessions();
        assertNotNull(subscriberDao.getSubscriberSessions(1).get(0).getDisconnectionDate());
        assertNull(subscriberDao.getSubscriberSessions(1).get(1).getDisconnectionDate());
    }

    @Test
    public void getAllSubscriberTariffsByIdShouldReturnsCorrectServiceCountTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataTariffs();
        assertEquals(subscriberDao.getSubscriberTariffs(1).size(), 2);
        assertEquals(subscriberDao.getSubscriberTariffs(2).size(), 0);
    }

    @Test
    public void getAllSubscriberTariffsByIdShouldReturnsCorrectDataTest() throws SQLException {
        insertStubDataSubscribers();
        insertStubDataTariffs();
        assertNotNull(subscriberDao.getSubscriberTariffs(1).get(0).getDisconnectTariff());
        assertNull(subscriberDao.getSubscriberTariffs(1).get(1).getDisconnectTariff());
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
        executeQuery("INSERT INTO \"subscriber_tariff\" (\"id\", \"subscriber_id\", \"connection_date\", \"disconnection_date\", \"tariff_id\") VALUES(1, 1, '2015-04-02', '2015-07-28', 1);");
        executeQuery("INSERT INTO \"subscriber_tariff\" (\"id\", \"subscriber_id\", \"connection_date\", \"tariff_id\") VALUES(2, 1, '2015-09-02', 2);");
    }

    private void insertStubDataSessions() throws SQLException {
        executeQuery("INSERT INTO \"subscriber_session\" (\"id\", \"subscriber_id\", \"connection_date\", \"disconnection_date\") VALUES(1, 1, '2015-04-02', '2015-07-28');");
        executeQuery("INSERT INTO \"subscriber_session\" (\"id\", \"subscriber_id\", \"connection_date\") VALUES(2, 1, '2015-09-02');");
    }

    private static void executeQuery(String query) throws SQLException {
        Statement stmt = ConnectionManager.getManager().getConnection().createStatement();
        stmt.execute(query);
    }
}
