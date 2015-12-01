package org.lostfan.ktv.dao.impl.hsqldb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.ServicePriceDAO;
import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.utils.ConnectionManager;

public class HsqldbServicePriceDAO implements ServicePriceDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public ServicePrice getByServiceIdAndDate(int serviceId, LocalDate date) {
        ServicePrice servicePrice = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service_price\" where \"service_id\" = ? AND \"date\" = ?");
            preparedStatement.setInt(1, serviceId);
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                servicePrice = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return servicePrice;
    }

    public ServicePrice get(int id) {
        ServicePrice servicePrice = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service_price\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                servicePrice = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return servicePrice;
    }

    public List<ServicePrice> getAll() {
        List<ServicePrice> servicePrices = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"service_price\"");
            while (rs.next()) {
                servicePrices.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return servicePrices;
    }

    public void save(ServicePrice servicePrice) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"service_price\" (\"service_id\", \"date\", \"price\") VALUES(?, ?, ?)");
            preparedStatement.setInt(1, servicePrice.getServiceId());
            preparedStatement.setDate(2, Date.valueOf(servicePrice.getDate()));
            preparedStatement.setInt(3, servicePrice.getPrice());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(ServicePrice servicePrice) {
        if(get(servicePrice.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"service_price\" set \"service_id\" = ?, \"date\" = ?, \"price\" = ? where \"id\" = ?");
                preparedStatement.setInt(1, servicePrice.getServiceId());
                preparedStatement.setDate(2, Date.valueOf(servicePrice.getDate()));
                preparedStatement.setInt(3, servicePrice.getPrice());
                preparedStatement.setInt(4, servicePrice.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }


    public void deleteByServiceIdAndDate(int serviceId, LocalDate date) {

        if(getByServiceIdAndDate(serviceId, date) != null)
        {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"service_price\" where \"service_id\" = ? AND \"date\" = ?");
                preparedStatement.setInt(1, serviceId);
                preparedStatement.setDate(2, Date.valueOf(date));
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public void delete(int servicePriceId) {
        if(get(servicePriceId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"service_price\" where \"id\" = ?");
                preparedStatement.setInt(1, servicePriceId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public Integer getPriceByDate(int serviceId, LocalDate date) {
        Integer price = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "SELECT TOP 1 * FROM \"service_price\" where \"service_id\" = ? AND \"date\" <= ? ORDER BY \"date\" DESC");
            preparedStatement.setInt(1, serviceId);
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                price = rs.getInt("price");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return price;
    }

    @Override
    public List<ServicePrice> getAllContainsInName(String str) {
        List<ServicePrice> servicePrices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service_id\" where LOWER(\"id\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                servicePrices.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return servicePrices;
    }

    public List<ServicePrice> getServicePricesByServiceId(int serviceId) {
        List<ServicePrice> servicePrices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service_price\" where \"service_id\" = ?");
            preparedStatement.setInt(1, serviceId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ServicePrice servicePrice = new ServicePrice();
//                servicePrice.setSelectedId(rs.getInt("id"));
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

    private ServicePrice constructEntity(ResultSet rs) throws SQLException{
        ServicePrice servicePrice = new ServicePrice();
        servicePrice.setId(rs.getInt("id"));
        servicePrice.setServiceId(rs.getInt("service_id"));
        servicePrice.setDate(rs.getDate("date").toLocalDate());
        servicePrice.setPrice(rs.getInt("price"));
        return servicePrice;
    }
}
