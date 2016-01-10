package org.lostfan.ktv.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionManager extends ConnectionManager {

    private Connection connection;

    public H2ConnectionManager() {
        try {
            Class.forName("org.h2.river");
            this.connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/D:/KTV/DB/NEW", "SA", "");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void close() {
        try {
            this.connection.createStatement().execute("SHUTDOWN");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
