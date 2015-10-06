package org.lostfan.ktv;

import org.lostfan.ktv.controller.MainController;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;
import org.lostfan.ktv.view.MainView;

public class Application {

    public static void main(String[] args) {
        ConnectionManager.setManager(new HsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());

        MainView view = new MainView();
        MainController controller = new MainController(view);
    }
}
