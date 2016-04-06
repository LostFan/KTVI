package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.DAOException;
import org.lostfan.ktv.dao.DisconnectionReasonDAO;
import org.lostfan.ktv.domain.DisconnectionReason;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostGreDisconnectionReasonDAO extends PostgreBaseDao implements DisconnectionReasonDAO {

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
            throw new DAOException();
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
            throw new DAOException();
        }
        return null;
    }

    @Override
    public void save(DisconnectionReason disconnectionReason) {
        try {
            PreparedStatement preparedStatement;
            if (disconnectionReason.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"disconnection_reason\" (\"name\", \"id\") VALUES(?, ?)");
                preparedStatement.setInt(2, disconnectionReason.getId());

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
            throw new DAOException();
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
            throw new DAOException();
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
            throw new DAOException();
        }
    }

    @Override
    public List<DisconnectionReason> getAllContainsInName(String str) {
        List<DisconnectionReason> materials = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"disconnection_reason\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                materials.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return materials;
    }

    private DisconnectionReason constructEntity(ResultSet resultSet) throws SQLException{
        DisconnectionReason disconnectionReason = new DisconnectionReason();
        disconnectionReason.setId(resultSet.getInt("id"));
        disconnectionReason.setName(resultSet.getString("name"));
        return disconnectionReason;
    }

}
