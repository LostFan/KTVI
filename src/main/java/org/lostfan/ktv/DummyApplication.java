package org.lostfan.ktv;


import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.domain.Service;

import java.util.List;

public class DummyApplication {

    public static void main(String[] args) {

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
