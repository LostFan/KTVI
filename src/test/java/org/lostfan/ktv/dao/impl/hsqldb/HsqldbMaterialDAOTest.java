package org.lostfan.ktv.dao.impl.hsqldb;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.MaterialDAO;
import org.lostfan.ktv.domain.Material;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.TestHsqldbConnectionManager;

import static org.junit.Assert.assertEquals;
import static org.lostfan.ktv.utils.DatabaseUtils.executeQuery;
import static org.lostfan.ktv.utils.DatabaseUtils.executeSqlFile;

/**
 * Created by Ihar_Niakhlebau on 24-Sep-15.
 */
public class HsqldbMaterialDAOTest {

    private static MaterialDAO materialDao;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        ConnectionManager.setManager(new TestHsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());
        materialDao = DAOFactory.getDefaultDAOFactory().getMaterialDAO();
        executeSqlFile("/hsqldb/drop.sql");
        executeSqlFile("/hsqldb/create.sql");
    }

    @Before
    public void clearTable() throws SQLException {
        executeQuery("DELETE FROM \"material_consumption\"");
        executeQuery("DELETE FROM \"rendered_service\"");
        executeQuery("DELETE FROM \"material\"");
        executeQuery("DELETE FROM \"service\"");
        executeQuery("DELETE FROM \"subscriber\"");
        executeQuery("ALTER TABLE \"material\" ALTER COLUMN \"id\" RESTART WITH 1");
        executeQuery("ALTER TABLE \"material_consumption\" ALTER COLUMN \"id\" RESTART WITH 1");
    }

    @Test
    public void emptyMaterialsTest() {
        assertEquals(materialDao.getAllMaterials().size(), 0);
    }

    @Test
    public void getAllServicesReturnsCorrectMaterialCountTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getAllMaterials().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingMaterialsTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getAllMaterials().get(0).getId(), 1);
    }

    @Test
    public void getExistingMaterialByIdTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getMaterial(1).getId(), 1);
    }

    @Test
    public void createNewMaterialCorrectServiceCountTest() throws SQLException {
        insertStubData();
        Material material = new Material();
        material.setName("New");
        material.setUnit("m");
        material.setPrice(30000);
        materialDao.save(material);
        assertEquals(materialDao.getAllMaterials().size(), 4);
    }

    @Test
    public void createNewMaterialShouldMatchInsertedValuesTest() throws SQLException {
        insertStubData();
        Material material = new Material();
        material.setName("New");
        material.setUnit("m");
        material.setPrice(30000);
        materialDao.save(material);
        assertEquals(materialDao.getAllMaterials().get(3).getName(), "New");
        assertEquals(materialDao.getAllMaterials().get(3).getPrice(), 30000);
        assertEquals(materialDao.getAllMaterials().get(3).getUnit(), "m");
    }

    @Test
    public void createNewMaterialShouldHaveIncrementedIdTest() throws SQLException {
        insertStubData();
        Material material = new Material();
        material.setName("New");
        material.setUnit("m");
        material.setPrice(30000);
        materialDao.save(material);
        assertEquals(materialDao.getAllMaterials().get(3).getId(), 4);
    }

    @Test
    public void updateExistingMaterialByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubData();
        Material material = materialDao.getMaterial(1);
        material.setPrice(2);
        materialDao.update(material);
        assertEquals(materialDao.getMaterial(1).getPrice(), 2);
    }

    @Test
    public void updateExistingMaterialByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        Material material = materialDao.getMaterial(1);
        material.setName("New");
        material.setPrice(2);
        materialDao.update(material);
        assertEquals(materialDao.getAllMaterials().size(), 3);
    }

    @Test
    public void updateExistingMaterialByIdShouldMatchNotUpdatedValuesTest() throws SQLException {
        insertStubData();
        Material material = materialDao.getMaterial(1);
        material.setPrice(2);
        materialDao.update(material);
        assertEquals(materialDao.getMaterial(1).getName(), "One");
    }

    @Test
    public void deleteMaterialByIdCorrectServiceCountTest() throws SQLException {
        insertStubDataMaterials();
        materialDao.delete(1);
        assertEquals(materialDao.getAllMaterials().size(), 2);
    }

    @Test
    public void deleteMaterialByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubDataMaterials();
        materialDao.delete(1);
        assertEquals(materialDao.getMaterial(1), null);
    }

    @Test
    public void getAllMaterialConsumptionsByRenderedServiceIdShouldReturnsCorrectCountTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getMaterialConsumptionsByRenderedServiceId(1).size(), 2);
        assertEquals(materialDao.getMaterialConsumptionsByRenderedServiceId(2).size(), 1);
        assertEquals(materialDao.getMaterialConsumptionsByRenderedServiceId(3).size(), 0);
    }

    @Test
    public void getAllMaterialConsumptionsByRenderedServiceIdShouldReturnsCorrectDataTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getMaterialConsumptionsByRenderedServiceId(1).get(0).getId(), 1);
        assertEquals(materialDao.getMaterialConsumptionsByRenderedServiceId(2).get(0).getId(), 3);
    }

    @Test
    public void getAllServicesReturnsCorrectMaterialConsumptionCountTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getAllMaterialConsumptions().size(), 3);
    }

    @Test
    public void getAllServicesReturnsAllExistingMaterialConsumptionsTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getAllMaterialConsumptions().get(0).getId(), 1);
    }

    @Test
    public void getExistingMaterialConsumptionByIdTest() throws SQLException {
        insertStubData();
        assertEquals(materialDao.getMaterialConsumption(1).getId(), 1);
    }

    @Test
    public void createNewMaterialConsumptionCorrectServiceCountTest() throws SQLException {
        insertStubData();
        MaterialConsumption materialConsumption = new MaterialConsumption();
        materialConsumption.setAmount(1);
        materialConsumption.setRenderedServiceId(1);
        materialConsumption.setMaterialId(1);
        materialDao.saveMaterialConsumption(materialConsumption);
        assertEquals(materialDao.getAllMaterialConsumptions().size(), 4);
    }

    @Test
    public void createNewMaterialConsumptionShouldMatchInsertedValuesTest() throws SQLException {
        insertStubData();
        MaterialConsumption materialConsumption = new MaterialConsumption();
        materialConsumption.setAmount(1);
        materialConsumption.setRenderedServiceId(1);
        materialConsumption.setMaterialId(1);
        materialDao.saveMaterialConsumption(materialConsumption);
        assertEquals(materialDao.getAllMaterialConsumptions().get(3).getRenderedServiceId(),1);
        assertEquals(materialDao.getAllMaterialConsumptions().get(3).getMaterialId(), 1);
    }

    @Test
    public void createNewMaterialConsumptionShouldHaveIncrementedIdTest() throws SQLException {
        insertStubData();
        MaterialConsumption materialConsumption = new MaterialConsumption();
        materialConsumption.setAmount(1);
        materialConsumption.setRenderedServiceId(1);
        materialConsumption.setMaterialId(1);
        materialDao.saveMaterialConsumption(materialConsumption);
        assertEquals(materialDao.getAllMaterialConsumptions().get(3).getId(), 4);
    }

    @Test
    public void updateExistingMaterialConsumptionByIdShouldMatchUpdatedValuesTest() throws SQLException {
        insertStubData();
        MaterialConsumption materialConsumption = materialDao.getMaterialConsumption(1);
        materialConsumption.setMaterialId(2);
        materialDao.updateMaterialConsumption(materialConsumption);
        assertEquals(materialDao.getMaterialConsumption(1).getMaterialId(), 2);
    }

    @Test
    public void updateExistingMaterialConsumptionByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        MaterialConsumption materialConsumption = materialDao.getMaterialConsumption(1);
        materialConsumption.setMaterialId(2);
        materialDao.updateMaterialConsumption(materialConsumption);
        assertEquals(materialDao.getAllMaterialConsumptions().size(), 3);
    }

    @Test
    public void updateExistingMaterialConsumptionByIdShouldMatchNotUpdatedValuesTest() throws SQLException {
        insertStubData();
        MaterialConsumption materialConsumption = materialDao.getMaterialConsumption(1);
        materialConsumption.setMaterialId(2);
        materialDao.updateMaterialConsumption(materialConsumption);
        assertEquals(materialDao.getMaterialConsumption(1).getRenderedServiceId(), 1);
    }

    @Test
    public void deleteMaterialConsumptionByIdCorrectServiceCountTest() throws SQLException {
        insertStubData();
        materialDao.deleteMaterialConsumption(1);
        assertEquals(materialDao.getAllMaterialConsumptions().size(), 2);
    }

    @Test
    public void deleteMaterialConsumptionByIdShouldDeleteCorrectDataTest() throws SQLException {
        insertStubData();
        materialDao.deleteMaterialConsumption(1);
        assertEquals(materialDao.getMaterialConsumption(1), null);
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

    private void insertStubDataMaterials() throws SQLException {
        executeQuery("INSERT INTO \"material\" (\"id\", \"name\", \"price\", \"unit\") VALUES(1, 'One', 100, 'm');");
        executeQuery("INSERT INTO \"material\" (\"id\", \"name\", \"price\", \"unit\") VALUES(2, 'Two', 50, 'm');");
        executeQuery("INSERT INTO \"material\" (\"id\", \"name\", \"price\", \"unit\") VALUES(3, 'Three', 25, 'm');");
    }

    private void insertStubDataMaterialConsumptions() throws SQLException {
        executeQuery("INSERT INTO \"material_consumption\" (\"id\", \"material_id\", \"rendered_service_id\", \"amount\") VALUES(1, 1, 1, 5);");
        executeQuery("INSERT INTO \"material_consumption\" (\"id\", \"material_id\", \"rendered_service_id\", \"amount\") VALUES(2, 1, 1, 10);");
        executeQuery("INSERT INTO \"material_consumption\" (\"id\", \"material_id\", \"rendered_service_id\", \"amount\") VALUES(3, 1, 2, 2);");
    }

    private void insertStubData() throws SQLException{
        insertStubDataServices();
        insertStubDataSubscribers();
        insertStubDataRenderedServices();
        insertStubDataMaterials();
        insertStubDataMaterialConsumptions();
    }
}
