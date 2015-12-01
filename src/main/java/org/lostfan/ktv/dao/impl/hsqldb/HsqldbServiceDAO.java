package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HsqldbServiceDAO implements ServiceDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"service\"");
            while (rs.next()) {
                services.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return services;
    }

    public Service get(int id) {
        Service service = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            service = constructEntity(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return service;
    }



    public void save(Service service) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"service\" (\"id\", \"name\", \"additional\", \"consume_materials\"" +
                            ", \"change_tariff\", \"connection_service\", \"disconnection_service\") VALUES(?, ?, ?, ?, ?, ?, ?)");
            if (service.getId() != null) {
                preparedStatement.setInt(1, service.getId());
            }
            preparedStatement.setString(2, service.getName());
            preparedStatement.setBoolean(3, service.isAdditionalService());
            preparedStatement.setBoolean(4, service.isConsumeMaterials());
            preparedStatement.setBoolean(5, service.isChangeTariff());
            preparedStatement.setBoolean(6, service.isConnectionService());
            preparedStatement.setBoolean(7, service.isDisconnectionService());
            preparedStatement.executeUpdate();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CALL IDENTITY()");
            resultSet.next();
            service.setId(resultSet.getInt(1));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Service service) {
        if(get(service.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"service\" set \"name\" = ?, \"additional\" = ?, \"consume_materials\" = ?, " +
                                " \"change_tariff\" = ?, \"connection_service\" = ?, \"disconnection_service\" = ? where \"id\" = ?");
                preparedStatement.setString(1, service.getName());
                preparedStatement.setBoolean(2, service.isAdditionalService());
                preparedStatement.setBoolean(3, service.isConsumeMaterials());
                preparedStatement.setBoolean(4, service.isChangeTariff());
                preparedStatement.setBoolean(5, service.isConnectionService());
                preparedStatement.setBoolean(6, service.isDisconnectionService());
                preparedStatement.setInt(7, service.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int serviceId) {
        if(get(serviceId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"service\" where \"id\" = ?");
                preparedStatement.setInt(1, serviceId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public List<Service> getServicesByBeginningPartOfName(String str) {
        List<Service> services = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, (str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                services.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return services;
    }

    @Override
    public List<Service> getAllContainsInName(String str) {
        List<Service> services = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                services.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return services;
    }

    private Service constructEntity(ResultSet rs) throws SQLException {
        Service service;
        service = new Service();
        service.setId(rs.getInt("id"));
        service.setName(rs.getString("name"));
        service.setAdditionalService(rs.getBoolean("additional"));
        service.setChangeTariff(rs.getBoolean("change_tariff"));
        service.setConsumeMaterials(rs.getBoolean("consume_materials"));
        service.setConnectionService(rs.getBoolean("connection_service"));
        service.setDisconnectionService(rs.getBoolean("disconnection_service"));
        return service;
    }
}
