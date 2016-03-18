package org.lostfan.ktv.dao.impl.postgre;

import java.sql.Connection;
import java.sql.SQLException;

import org.lostfan.ktv.utils.ConnectionManager;

public abstract class PostgreBaseDao {

    protected Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public void transactionBegin() {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
