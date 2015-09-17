package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HsqldbServiceDAO implements ServiceDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"service\"");
            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getInt("id"));
                service.setName(rs.getString("name"));
                service.setAdditionalService(rs.getBoolean("additional"));

                services.add(service);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return services;
    }

    public Service getService(int id) {
        Service service = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                service = new Service();
                service.setId(rs.getInt("id"));
                service.setName(rs.getString("name"));
                service.setAdditionalService(rs.getBoolean("additional"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return service;
    }

    public void save(Service service) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"service\" (\"name\", \"additional\") VALUES(?, ?)");
            preparedStatement.setString(1, service.getName());
            preparedStatement.setBoolean(2, service.isAdditionalService());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Service service) {
        if(getService(service.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"service\" set \"name\" = ?, \"additional\" = ? where \"id\" = ?");
                preparedStatement.setString(1, service.getName());
                preparedStatement.setBoolean(2, service.isAdditionalService());
                preparedStatement.setInt(3, service.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int serviceId) {
        if(getService(serviceId) != null) {
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

    public int getCostByDay(Service service, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<ServicePrice> getServicePricesByServiceId(int serviceId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
