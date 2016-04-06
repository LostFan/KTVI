package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.DAOException;
import org.lostfan.ktv.dao.StreetDAO;
import org.lostfan.ktv.domain.Street;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostGreStreetDAO extends PostgreBaseDao implements StreetDAO {

    public List<Street> getAll() {
        List<Street> streets = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"street\"  order by \"id\" asc limit 100");
            while (rs.next()) {
                Street street = constructEntity(rs);

                streets.add(street);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return streets;
    }

    public Street get(int id) {
        Street street = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"street\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                street = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return street;
    }

    public void save(Street street) {
        try {
            PreparedStatement preparedStatement;
            if (street.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"street\" (\"name\", \"id\") VALUES(?, ?)");
                preparedStatement.setInt(2, street.getId());
            }
             else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"street\" (\"name\") VALUES(?)");
            }
            preparedStatement.setString(1, street.getName());
            preparedStatement.executeUpdate();
            if(street.getId() != null) {
                return;
            }
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT lastval() ");
            if (resultSet.next()) {
                street.setId(resultSet.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }
    }

    public void update(Street street) {
        if(get(street.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"street\" set \"name\" = ? where \"id\" = ?");
                preparedStatement.setString(1, street.getName());
                preparedStatement.setInt(2, street.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException();
            }
        }
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
                throw new DAOException();
            }
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
            throw new DAOException();
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
            throw new DAOException();
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
