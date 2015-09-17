package org.lostfan.ktv.utils;

import java.sql.Connection;

public abstract  class ConnectionManager {

    private static ConnectionManager manager;

    public static void setManager(ConnectionManager manager) {
        ConnectionManager.manager = manager;
    }

    public static ConnectionManager getManager() {
        return manager;
    }

    public abstract Connection getConnection();
}
