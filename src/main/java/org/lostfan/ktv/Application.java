package org.lostfan.ktv;

import org.lostfan.ktv.controller.MainController;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;
import org.lostfan.ktv.view.MainView;

public class Application {

    public static void main(String[] args) {
        ConnectionManager.setManager(new HsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());

        // Close connections and save all the changes
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ConnectionManager.getManager().close();
            }
        });

        MainModel model = new MainModel();
        MainView view = new MainView(model);
        MainController controller = new MainController(model, view);
    }
}
