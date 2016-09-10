package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.DAOException;
import org.lostfan.ktv.dao.MaterialDAO;
import org.lostfan.ktv.domain.Material;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostGreMaterialDAO extends PostgreBaseDao implements MaterialDAO {

    public List<Material> getAll() {
        List<Material> materials = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"material\"");
            while (rs.next()) {
                materials.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return materials;
    }

    public Material get(int id) {
        Material material = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"material\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                material = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return material;
    }


    public void save(Material material) {
        try {
            PreparedStatement preparedStatement;
            if (material.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"material\" (\"name\", \"price\", \"unit\", \"id\") VALUES(?, ?, ?, ?)");
                preparedStatement.setInt(4, material.getId());
            } else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"material\" (\"name\", \"price\", \"unit\") VALUES(?, ?, ?)");
            }
            preparedStatement.setString(1, material.getName());
            preparedStatement.setBigDecimal(2, material.getPrice());
            preparedStatement.setString(3, material.getUnit());
            preparedStatement.executeUpdate();
            if (material.getId() != null) {
                return;
            }
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT lastval()");
            resultSet.next();
            material.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }
    }

    public void update(Material material) {
        if (get(material.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"material\" set \"name\" = ?, \"price\" = ?, \"unit\" = ? where \"id\" = ?");
                preparedStatement.setString(1, material.getName());
                preparedStatement.setBigDecimal(2, material.getPrice());
                preparedStatement.setString(3, material.getUnit());
                preparedStatement.setInt(4, material.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException();
            }
        }
    }

    public void delete(int id) {
        if(get(id) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"material\" where \"id\" = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException();
            }
        } else {
            throw new DAOException();
        }
    }

    @Override
    public List<Material> getAllContainsInName(String str) {
        List<Material> materials = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"material\" where LOWER(\"name\") LIKE ?");
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

    private Material constructEntity(ResultSet rs) throws SQLException {
        Material material;
        material = new Material();
        material.setId(rs.getInt("id"));
        material.setPrice(rs.getBigDecimal("price"));
        material.setName(rs.getString("name"));
        material.setUnit(rs.getString("unit"));
        return material;
    }
}
