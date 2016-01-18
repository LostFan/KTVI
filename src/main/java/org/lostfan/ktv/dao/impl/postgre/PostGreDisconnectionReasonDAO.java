package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.DisconnectionReasonDAO;
import org.lostfan.ktv.domain.DisconnectionReason;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostGreDisconnectionReasonDAO implements DisconnectionReasonDAO {

    @Override
    public List<DisconnectionReason> getAll() {
        List<DisconnectionReason> reasons = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"disconnection_reason\"");

            while (resultSet.next()) {
                reasons.add(constructEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reasons;
    }

    @Override
    public DisconnectionReason get(int id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"disconnection_reason\" WHERE \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return constructEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(DisconnectionReason disconnectionReason) {
        try {
            PreparedStatement preparedStatement;
            if (disconnectionReason.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"disconnection_reason\" (\"name\", \"id\") VALUES(?, ?); " +
                                "ALTER SEQUENCE serial_disconnection_reason RESTART WITH ?;");
                preparedStatement.setInt(2, disconnectionReason.getId());
                preparedStatement.setInt(3, disconnectionReason.getId() + 1);
            } else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"disconnection_reason\" (\"name\") VALUES(?)");
            }
            preparedStatement.setString(1, disconnectionReason.getName());
            preparedStatement.executeUpdate();
            if(disconnectionReason.getId() != null) {
                return;
            }
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT lastval()");
            if (resultSet.next()) {
                disconnectionReason.setId(resultSet.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(DisconnectionReason disconnectionReason) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "UPDATE \"disconnection_reason\" set \"name\" = ? where \"id\" = ?");
            preparedStatement.setString(1, disconnectionReason.getName());
            preparedStatement.setInt(2, disconnectionReason.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "DELETE FROM  \"disconnection_reason\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<DisconnectionReason> getAllContainsInName(String str) {
        throw new UnsupportedOperationException("getAllContainsInName is not implemented");
    }

    private DisconnectionReason constructEntity(ResultSet resultSet) throws SQLException{
        DisconnectionReason disconnectionReason = new DisconnectionReason();
        disconnectionReason.setId(resultSet.getInt("id"));
        disconnectionReason.setName(resultSet.getString("name"));
        return disconnectionReason;
    }

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }
}
