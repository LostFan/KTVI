package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.StreetDAO;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HsqldbStreetDAO implements StreetDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<Street> getAll() {
        List<Street> streets = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"street\"");
            while (rs.next()) {
                Street street = constructEntity(rs);

                streets.add(street);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return streets;
    }

    public Street get(int id) {
        Street street = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"street\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            street = constructEntity(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return street;
    }

    public Street save(Street street) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"street\" (\"name\") VALUES(?)");
            preparedStatement.setString(1, street.getName());
            preparedStatement.executeUpdate();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CALL IDENTITY()");
            Integer id = null;
            resultSet.next();
            street.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return street;
    }

    public Street update(Street street) {
        if(get(street.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"street\" set \"name\" = ? where \"id\" = ?");
                preparedStatement.setString(1, street.getName());
                preparedStatement.setInt(2, street.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
        return street;
    }

    public void delete(int streetId) {
        if(get(streetId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"street\" where \"id\" = ?");
                preparedStatement.setInt(1, streetId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public List<Street> getStreetsByBeginningPartOfName(String str) {
        List<Street> streets = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"street\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, (str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                streets.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return streets;
    }

    @Override
    public List<Street> getAllContainsInName(String str) {
        List<Street> streets = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"street\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                streets.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return streets;
    }

    private Street constructEntity(ResultSet rs) throws SQLException {
        Street street = new Street();
        street.setId(rs.getInt("id"));
        street.setName(rs.getString("name"));
        return street;
    }
}
