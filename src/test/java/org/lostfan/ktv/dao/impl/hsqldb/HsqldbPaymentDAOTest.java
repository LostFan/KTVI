package org.lostfan.ktv.dao.impl.hsqldb;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.PaymentType;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.TestHsqldbConnectionManager;

import static org.junit.Assert.assertEquals;
import static org.lostfan.ktv.utils.DatabaseUtils.executeQuery;
import static org.lostfan.ktv.utils.DatabaseUtils.executeSqlFile;

public class HsqldbPaymentDAOTest {

    private static PaymentDAO paymentDao;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        ConnectionManager.setManager(new TestHsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());
        paymentDao = DAOFactory.getDefaultDAOFactory().getPaymentDAO();
        executeSqlFile("/hsqldb/drop.sql");
        executeSqlFile("/hsqldb/create.sql");
    }

    @Before
    public void clearTable() throws SQLException {
        executeQuery("DELETE FROM \"payment\"");
        executeQuery("DELETE FROM \"payment_type\"");
        executeQuery("DELETE FROM \"service\"");
        executeQuery("DELETE FROM \"subscriber\"");
        executeQuery("ALTER TABLE \"payment\" ALTER COLUMN \"id\" RESTART WITH 1");
        executeQuery("ALTER TABLE \"payment_type\" ALTER COLUMN \"id\" RESTART WITH 1");
    }

    @Test
    public void emptyPaymentsTest() {
        assertEquals(paymentDao.getAll().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectPaymentCountTest() throws SQLException {
        insertStubData();
        assertEquals(paymentDao.getAll().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingPaymentsTest() throws SQLException {
        insertStubData();
        assertEquals(paymentDao.getAll().get(0).getId().intValue(), 1);
    }

    @Test
    public void getExistingPaymentByIdTest() throws SQLException {
        insertStubData();
        assertEquals(paymentDao.get(1).getId().intValue(), 1);
    }

    @Test
    public void createNewPaymentCorrectServiceCountTest() throws SQLException {
        insertStubData();
        Payment payment = new Payment();
        payment.setServicePaymentId(1);
        payment.setSubscriberAccount(3);
        payment.setDate(LocalDate.of(2015, 11, 11));
        paymentDao.save(payment);
        assertEquals(paymentDao.getAll().size(), 4);
    }

    @Test
    public void createNewPaymentShouldMatchInsertedValuesTest() throws SQLException {
        insertStubData();
        Payment payment = new Payment();
        payment.setServicePaymentId(1);
        payment.setSubscriberAccount(3);
        payment.setDate(LocalDate.of(2015, 11, 11));
        paymentDao.save(payment);
        assertEquals(paymentDao.getAll().get(3).getServicePaymentId().intValue(), 1);
        assertEquals(paymentDao.getAll().get(3).getSubscriberAccount(), 3);
        assertEquals(paymentDao.getAll().get(3).getDate(), LocalDate.of(2015, 11, 11));
    }

    @Test
    public void createNewPaymentShouldHaveIncrementedIdTest() throws SQLException {
        insertStubData();
        Payment payment = new Payment();
        payment.setServicePaymentId(1);
        payment.setSubscriberAccount(3);
        payment.setDate(LocalDate.of(2015, 11, 11));
        paymentDao.save(payment);
        assertEquals(paymentDao.getAll().get(3).getId().intValue(), 4);
    }

    @Test
    public void updateExistingPaymentByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubData();
        Payment payment = paymentDao.get(1);
        payment.setServicePaymentId(2);
        paymentDao.update(payment);
        assertEquals(paymentDao.get(1).getServicePaymentId().intValue(), 2);
    }

    @Test
    public void updateExistingPaymentByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        Payment payment = paymentDao.get(1);
        payment.setServicePaymentId(2);
        payment.setSubscriberAccount(2);
        paymentDao.update(payment);
        assertEquals(paymentDao.getAll().size(), 3);
    }

    @Test
    public void updateExistingPaymentByIdShouldMatchNotUpdatedValuesTest() throws SQLException {
        insertStubData();
        Payment payment = paymentDao.get(1);
        payment.setServicePaymentId(2);
        paymentDao.update(payment);
        assertEquals(paymentDao.get(1).getSubscriberAccount(), 1);
    }

    @Test
    public void deletePaymentByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        paymentDao.delete(1);
        assertEquals(paymentDao.getAll().size(), 2);
    }

    @Test
    public void deletePaymentByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubData();
        paymentDao.delete(1);
        assertEquals(paymentDao.get(1), null);
    }

    @Test
    public void getAllPaymentsBySubscriberIdShouldReturnsCorrectCountTest() throws SQLException {
        insertStubData();
        assertEquals(paymentDao.getPaymentsBySubscriberId(1).size(), 2);
        assertEquals(paymentDao.getPaymentsBySubscriberId(2).size(), 1);
        assertEquals(paymentDao.getPaymentsBySubscriberId(3).size(), 0);
    }

    @Test
    public void getAllPaymentsBySubscriberIdShouldReturnsCorrectDataTest() throws SQLException {
        insertStubData();
        assertEquals(paymentDao.getPaymentsBySubscriberId(1).get(0).getServicePaymentId().intValue(), 1);
        assertEquals(paymentDao.getPaymentsBySubscriberId(2).get(0).getServicePaymentId().intValue(), 3);
    }

    @Test
    public void getAllPaymentsByDateShouldReturnsCorrectCountTest() throws SQLException {
        insertStubData();
        assertEquals(paymentDao.getPaymentsByDate(LocalDate.of(2015, 8, 3)).size(), 2);
        assertEquals(paymentDao.getPaymentsByDate(LocalDate.of(2015, 6, 27)).size(), 1);
        assertEquals(paymentDao.getPaymentsByDate(LocalDate.of(2015, 1, 1)).size(), 0);
    }

    @Test
    public void getAllPaymentsByDateShouldReturnsCorrectDataTest() throws SQLException {
        insertStubData();
        assertEquals(paymentDao.getPaymentsByDate(LocalDate.of(2015, 6, 27)).get(0).getServicePaymentId().intValue(), 2);
        assertEquals(paymentDao.getPaymentsByDate(LocalDate.of(2015, 6, 27)).get(0).getSubscriberAccount(), 1);
    }

    @Test
    public void emptyPaymentTypesTest() {
        assertEquals(paymentDao.getAllPaymentTypes().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectPaymentTypeCountTest() throws SQLException {
        insertStubDataPaymentTypes();
        assertEquals(paymentDao.getAllPaymentTypes().size(), 2);
    }

    @Test
    public void getAllServicesReturnsAllExistingPaymentTypesTest() throws SQLException {
        insertStubDataPaymentTypes();
        assertEquals(paymentDao.getAllPaymentTypes().get(0).getId().intValue(), 1);
    }

    @Test
    public void getExistingPaymentTypeByIdTest() throws SQLException {
        insertStubDataPaymentTypes();
        assertEquals(paymentDao.getPaymentType(1).getId().intValue(), 1);
    }

    @Test
    public void createNewPaymentTypeCorrectServiceCountTest() throws SQLException {
        insertStubDataPaymentTypes();
        PaymentType paymentType = new PaymentType();
        paymentType.setName("New");
        paymentDao.savePaymentType(paymentType);
        assertEquals(paymentDao.getAllPaymentTypes().size(), 3);
    }

    @Test
    public void createNewPaymentTypeShouldMatchInsertedValuesTest() throws SQLException {
        insertStubDataPaymentTypes();
        PaymentType paymentType = new PaymentType();
        paymentType.setName("New");
        paymentDao.savePaymentType(paymentType);
        assertEquals(paymentDao.getAllPaymentTypes().get(2).getName(), "New");
    }

    @Test
    public void createNewPaymentTypeShouldHaveIncrementedIdTest() throws SQLException {
        insertStubDataPaymentTypes();
        PaymentType paymentType = new PaymentType();
        paymentType.setName("New");
        paymentDao.savePaymentType(paymentType);
        assertEquals(paymentDao.getAllPaymentTypes().get(2).getId().intValue(), 3);
    }

    @Test
    public void updateExistingPaymentTypeByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubDataPaymentTypes();
        PaymentType paymentType = paymentDao.getPaymentType(1);
        paymentType.setName("New");
        paymentDao.updatePaymentType(paymentType);
        assertEquals(paymentDao.getPaymentType(1).getName(), "New");
    }

    @Test
    public void updateExistingPaymentTypeByIdCorrectServiceCountTest() throws SQLException {
        insertStubDataPaymentTypes();
        PaymentType paymentType = paymentDao.getPaymentType(1);
        paymentType.setName("New");
        paymentDao.updatePaymentType(paymentType);
        assertEquals(paymentDao.getAllPaymentTypes().size(), 2);
    }

    @Test
    public void deletePaymentTypeByIdCorrectServiceCountTest() throws SQLException {
        insertStubDataPaymentTypes();
        paymentDao.deletePaymentType(1);
        assertEquals(paymentDao.getAllPaymentTypes().size(), 1);
    }

    @Test
    public void deletePaymentTypeByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubDataPaymentTypes();
        paymentDao.deletePaymentType(1);
        assertEquals(paymentDao.getPaymentType(1), null);
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

    private void insertStubDataPaymentTypes() throws SQLException {
        executeQuery("INSERT INTO \"payment_type\" (\"id\", \"name\") VALUES(1, 'Post');");
        executeQuery("INSERT INTO \"payment_type\" (\"id\", \"name\") VALUES(2, 'Bank');");
    }

    private void insertStubDataPayments() throws SQLException {
        executeQuery("INSERT INTO \"payment\" (\"id\", \"subscriber_id\", \"service_id\", \"payment_type_id\", \"date\", \"price\") VALUES(1, 1, 1, 1, '2015-8-3', 40000);");
        executeQuery("INSERT INTO \"payment\" (\"id\", \"subscriber_id\", \"service_id\", \"payment_type_id\", \"date\", \"price\") VALUES(2, 1, 2, 1, '2015-6-27', 50000);");
        executeQuery("INSERT INTO \"payment\" (\"id\", \"subscriber_id\", \"service_id\", \"payment_type_id\", \"date\", \"price\") VALUES(3, 2, 3, 2,  '2015-8-3', 10000);");
    }

    private void insertStubData() throws SQLException{
        insertStubDataServices();
        insertStubDataSubscribers();
        insertStubDataPaymentTypes();
        insertStubDataPayments();
    }
}
