package org.lostfan.ktv;

import org.lostfan.ktv.controller.ServiceController;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.model.ServiceModel;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;
import org.lostfan.ktv.view.ServiceView;

public class Application {

    public static void main(String[] args) {
        ConnectionManager.setManager(new HsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());

        ServiceModel model = new ServiceModel();
        ServiceView view = new ServiceView(model);
        ServiceController controller = new ServiceController(model, view);
    }
}
