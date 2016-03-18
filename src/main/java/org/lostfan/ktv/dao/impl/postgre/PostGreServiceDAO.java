package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostGreServiceDAO extends PostgreBaseDao implements ServiceDAO {

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

            if (rs.next()) {
                service = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return service;
    }



    public void save(Service service) {
        try {
            PreparedStatement preparedStatement;
            if (service.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"service\" (\"name\", \"additional\", \"consume_materials\"" +
                                ", \"change_tariff\", \"connection_service\", \"disconnection_service\", \"id\") VALUES(?, ?, ?, ?, ?, ?, ?); " +
//                                "ALTER SEQUENCE serial_service RESTART WITH ?;");
                                "");
                preparedStatement.setInt(7, service.getId());
//                preparedStatement.setInt(8, service.getId() + 1);
            } else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"service\" (\"name\", \"additional\", \"consume_materials\"" +
                                ", \"change_tariff\", \"connection_service\", \"disconnection_service\") VALUES(?, ?, ?, ?, ?, ?)");
            }
            preparedStatement.setString(1, service.getName());
            preparedStatement.setBoolean(2, service.isAdditionalService());
            preparedStatement.setBoolean(3, service.isConsumeMaterials());
            preparedStatement.setBoolean(4, service.isChangeTariff());
            preparedStatement.setBoolean(5, service.isConnectionService());
            preparedStatement.setBoolean(6, service.isDisconnectionService());
            preparedStatement.executeUpdate();
            if (service.getId() != null) {
                return;
            }
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT lastval()");
            if (resultSet.next()) {
                service.setId(resultSet.getInt(1));
            }


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

    @Override
    public void savePrice(ServicePrice servicePrice) {
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

    public int getPriceByDate(int serviceId, LocalDate date) {
        if(get(serviceId) != null) {
            int price = 0;
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"service_price\" where \"service_id\" = ? AND \"date\" <= ?" +
                        " ORDER BY \"date\" DESC LIMIT 1");
                preparedStatement.setInt(1, serviceId);
                preparedStatement.setDate(2, Date.valueOf(date));
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    price =  rs.getInt("price");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
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

    public void saveServicePrice(ServicePrice servicePrice) {
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


    public void deleteServicePrice(ServicePrice servicePrice) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "DELETE FROM  \"service_price\" where \"service_id\" = ? AND \"date\" = ?");
            preparedStatement.setInt(1, servicePrice.getServiceId());
            preparedStatement.setDate(2, Date.valueOf(servicePrice.getDate()));
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<ServicePrice> getServicePrices(int serviceId) {
        List<ServicePrice> servicePrices = new ArrayList<>();
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM \"service_price\" WHERE \"service_id\"=?");
            statement.setInt(1, serviceId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                servicePrices.add(constructPriceEntity(rs));
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

    private ServicePrice constructPriceEntity(ResultSet rs) throws SQLException {
        ServicePrice servicePrice = new ServicePrice();
        servicePrice.setPrice(rs.getInt("price"));
        servicePrice.setServiceId(rs.getInt("service_id"));
        servicePrice.setDate(rs.getDate("date").toLocalDate());
        return servicePrice;
    }
}
