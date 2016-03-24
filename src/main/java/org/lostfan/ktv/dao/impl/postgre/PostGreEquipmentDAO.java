package org.lostfan.ktv.dao.impl.postgre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOException;
import org.lostfan.ktv.dao.EquipmentDAO;
import org.lostfan.ktv.domain.Equipment;

public class PostGreEquipmentDAO extends PostgreBaseDao implements EquipmentDAO {

    public List<Equipment> getAll() {
        List<Equipment> equipments = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"equipment\"");
            while (rs.next()) {
                equipments.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return equipments;
    }

    public Equipment get(int id) {
        Equipment equipment = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"equipment\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                equipment = constructEntity(rs);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return equipment;
    }


    public void save(Equipment equipment) {
        try {
            PreparedStatement preparedStatement;
            if (equipment.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"equipment\" (\"name\", \"price\", \"id\") VALUES(?, ?, ?); ");
//                                "ALTER SEQUENCE serial_equipment RESTART WITH ?;");
                preparedStatement.setInt(4, equipment.getId());
//                preparedStatement.setInt(5, equipment.getId() + 1);
            } else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"equipment\" (\"name\", \"price\") VALUES(?, ?)");
            }
            preparedStatement.setString(1, equipment.getName());
            preparedStatement.setInt(2, equipment.getPrice());
            preparedStatement.executeUpdate();
            if (equipment.getId() != null) {
                return;
            }
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT lastval()");
            resultSet.next();
            equipment.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }
    }

    public void update(Equipment equipment) {
        if (get(equipment.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"equipment\" set \"name\" = ?, \"price\" = ? where \"id\" = ?");
                preparedStatement.setString(1, equipment.getName());
                preparedStatement.setInt(2, equipment.getPrice());
                preparedStatement.setInt(3, equipment.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException();
            }
        } else {
//            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int id) {
        if (get(id) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"equipment\" where \"id\" = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException();
            }
        } else {
            throw new DAOException();
//            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    @Override
    public List<Equipment> getAllContainsInName(String str) {
        List<Equipment> equipments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"equipment\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                equipments.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
        }

        return equipments;
    }

    private Equipment constructEntity(ResultSet rs) throws SQLException {
        Equipment equipment;
        equipment = new Equipment();
        equipment.setId(rs.getInt("id"));
        equipment.setPrice(rs.getInt("price"));
        equipment.setName(rs.getString("name"));
        return equipment;
    }
}
