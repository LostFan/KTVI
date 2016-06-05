package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HsqldbRenderedServiceDAO extends HsqldbBaseDao implements RenderedServiceDAO {

    public List<RenderedService> getAll() {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"rendered_service\"");
            while (rs.next()) {
                RenderedService renderedService = constructEntity(rs);

                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    @Override
    public List<RenderedService> getByMonth(LocalDate date) {
        return null;
    }

    public RenderedService get(int id) {
        RenderedService renderedService = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                renderedService = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedService;
    }

    public List<RenderedService> getByDate(LocalDate date) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"date\" = ?");
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = constructEntity(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public List<RenderedService> getBySubscriber(int subscriberAccount) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"subscriber_account\" = ?");
            preparedStatement.setInt(1, subscriberAccount);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = constructEntity(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public List<RenderedService> getByService(int serviceId) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"service_id\" = ?");
            preparedStatement.setInt(1, serviceId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = constructEntity(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    @Override
    public List<RenderedService> getAll(int subscriberAccount, int serviceId) {
        return null;
    }

    public List<RenderedService> getAll(int serviceId, LocalDate date) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"service_id\" = ? AND \"date\" = ?");
            preparedStatement.setInt(1, serviceId);
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = constructEntity(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public List<RenderedService> getAllForMonth(int serviceId, LocalDate date) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"service_id\" = ? AND \"date\" >= ? AND \"date\" < ?");
            preparedStatement.setInt(1, serviceId);
            preparedStatement.setDate(2, Date.valueOf(date.withDayOfMonth(1)));
            preparedStatement.setDate(3, Date.valueOf(date.withDayOfMonth(1).plusMonths(1)));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = constructEntity(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public List<RenderedService> getAllForMonth(int serviceId, int subscriberAccount, LocalDate date) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"service_id\" = ? AND \"subscriber_account\" = ? AND \"date\" >= ? AND \"date\" < ?");
            preparedStatement.setInt(1, serviceId);
            preparedStatement.setInt(2, subscriberAccount);
            preparedStatement.setDate(3, Date.valueOf(date.withDayOfMonth(1)));
            preparedStatement.setDate(4, Date.valueOf(date.withDayOfMonth(1).plusMonths(1)));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = constructEntity(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public RenderedService getFirstRenderedServiceLessDate(int serviceId, int subscriberAccount, LocalDate date) {
        RenderedService renderedService = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT TOP 1 * FROM \"rendered_service\" where \"service_id\" = ? AND \"subscriber_account\" = ? AND \"date\" <= ? ORDER BY \"date\" DESC");
            preparedStatement.setInt(1, serviceId);
            preparedStatement.setInt(2, subscriberAccount);
            preparedStatement.setDate(3, Date.valueOf(date));;
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                renderedService = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedService;
    }


    public void save(RenderedService renderedService) {
        try {
            PreparedStatement preparedStatement;
            if(renderedService.getId() !=null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"rendered_service\" (\"subscriber_account\", \"service_id\", \"date\",  \"price\", \"id\")" +
                                " VALUES(?, ?, ?, ?, ?)");
                preparedStatement.setInt(5, renderedService.getId());
            } else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"rendered_service\" (\"subscriber_account\", \"service_id\", \"date\",  \"price\")" +
                                " VALUES(?, ?, ?, ?)");
            }

            preparedStatement.setInt(1, renderedService.getSubscriberAccount());
            preparedStatement.setInt(2, renderedService.getServiceId());
            preparedStatement.setDate(3, Date.valueOf(renderedService.getDate()));
            if (renderedService.getPrice() != null) {
                preparedStatement.setInt(4, renderedService.getPrice());
            } else {
                preparedStatement.setNull(4, Types.INTEGER);
            }
            preparedStatement.executeUpdate();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CALL IDENTITY()");
            resultSet.next();
            renderedService.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            System.out.println(renderedService.getSubscriberAccount());
            ex.printStackTrace();
        }
    }

    public void update(RenderedService renderedService) {
        if(get(renderedService.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"rendered_service\" set \"subscriber_account\" = ?, \"service_id\" = ?, \"date\" = ?, \"price\" = ? where \"id\" = ?");
                preparedStatement.setInt(1, renderedService.getSubscriberAccount());
                preparedStatement.setInt(2, renderedService.getServiceId());
                preparedStatement.setDate(3, Date.valueOf(renderedService.getDate()));
                preparedStatement.setInt(4, renderedService.getPrice());
                preparedStatement.setInt(5, renderedService.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int id) {
        if(get(id) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"rendered_service\" where \"id\" = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    @Override
    public List<RenderedService> getAllContainsInName(String str) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where LOWER(\"id\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                renderedServices.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    private RenderedService constructEntity(ResultSet rs) throws SQLException{
        RenderedService renderedService = new RenderedService();
        renderedService.setId(rs.getInt("id"));
        renderedService.setServiceId(rs.getInt("service_id"));
        renderedService.setSubscriberAccount(rs.getInt("subscriber_account"));
        renderedService.setPrice(rs.getInt("price"));
        renderedService.setDate(rs.getDate("date").toLocalDate());
        return renderedService;
    }

    @Override
    public Map<Integer, Integer> getAllRenderedServicesPriceInMonthForSubscriber(LocalDate date) {
        return null;
    }

    @Override
    public Map<Integer, Integer> getAllRenderedServicesPriceInMonthForSubscriberByServiceId(int serviceId, LocalDate date) {
        return null;
    }

    @Override
    public Map<Integer, Integer> getAllRenderedServicesPriceForSubscriberByServiceIdBeforeDate(int serviceId, LocalDate date) {
        return null;
    }
}
