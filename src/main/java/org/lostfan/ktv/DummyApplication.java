package org.lostfan.ktv;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;

import java.util.List;

public class DummyApplication {

    public static void main(String[] args) {

        // Register HSQLDB Connection
        ConnectionManager.setManager(new HsqldbConnectionManager());

        // Register DAO implementation
        // Should be at the application start
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());


        // Retrieve required DAO
        DAOFactory daoFactory = DAOFactory.getDefaultDAOFactory();
        ServiceDAO dao = daoFactory.getServiceDAO();

        // Get some data
        List<Service> services = dao.getAllServices();
        System.out.println("Services found: " + services.size());
    }
}
