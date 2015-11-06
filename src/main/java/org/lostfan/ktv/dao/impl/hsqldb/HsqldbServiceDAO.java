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

    public Service get(int id) {
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
        if(get(service.getId()) != null) {
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

    public int getPriceByDate(int serviceId, LocalDate date) {
        if(get(serviceId) != null) {
            int price = 0;
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT TOP 1 * FROM \"service_price\" where \"service_id\" = ? AND \"date\" <= ?" +
                        " ORDER BY \"date\" DESC");
                preparedStatement.setInt(1, serviceId);
                preparedStatement.setDate(2, Date.valueOf(date));
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    price =  rs.getInt("price");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if(price == 0) {
                throw new UnsupportedOperationException("Tariff not exist for this day");
            }
            return price;
        } else {
            throw new UnsupportedOperationException("Tariff not exist");
        }
    }

    public List<ServicePrice> getServicePricesByServiceId(int serviceId) {
        List<ServicePrice> servicePrices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service_price\" where \"service_id\" = ?");
            preparedStatement.setInt(1, serviceId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ServicePrice servicePrice = new ServicePrice();
//                servicePrice.setId(rs.getInt("id"));
                servicePrice.setServiceId(rs.getInt("service_id"));
                servicePrice.setPrice(rs.getInt("price"));
                servicePrice.setDate(rs.getDate("date").toLocalDate());
                servicePrices.add(servicePrice);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return servicePrices;
    }


    public List<Service> getServicesByBeginningPartOfName(String str) {
        List<Service> services = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, (str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
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
}
