package org.lostfan.ktv;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DummyApplication {

    public static void main(String[] args) {
/*
        // Register HSQLDB Connection
        ConnectionManager.setManager(new HsqldbConnectionManager());

        // Register DAO implementation
        // Should be at the application start
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());


        // Retrieve required DAO
        DAOFactory daoFactory = DAOFactory.getDefaultDAOFactory();
        ServiceDAO dao = daoFactory.getServiceDAO();

        // Get some data
        List<Service> services = dao.getAll();
        System.out.println("Services found: " + services.size());*/

        //String str = "Help ONLINE 24/7 and more...";
        String str = "11-5-2015";
        String patternStr = "(.*)(\\d{5})(.*)";

        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(str);
        //System.out.println(m.matches());

        if (m.find()) {
            System.out.println("Groups: " + m.groupCount());
            for (int i = 0; i < m.groupCount(); ++i) {
                System.out.println(i + " - " + m.group(i));
            }
        } else {
            System.out.println("No matches");
        }
    }
}
