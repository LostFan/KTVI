package org.lostfan.ktv;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lostfan.ktv.controller.MainController;
import org.lostfan.ktv.controller.ServiceController;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.ServiceModel;
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
