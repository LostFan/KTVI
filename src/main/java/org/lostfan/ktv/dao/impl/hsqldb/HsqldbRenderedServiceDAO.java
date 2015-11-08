package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HsqldbRenderedServiceDAO implements RenderedServiceDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<RenderedService> getAllRenderedServices() {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"rendered_service\"");
            while (rs.next()) {
                RenderedService renderedService = createRenderedService(rs);

                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public RenderedService getRenderedService(int id) {
        RenderedService renderedService = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                renderedService = createRenderedService(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedService;
    }

    public List<RenderedService> getRenderedServicesByDate(LocalDate date) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"date\" = ?");
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = createRenderedService(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public List<RenderedService> getRenderedServicesBySubscriberId(int subscriberId) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"subscriber_id\" = ?");
            preparedStatement.setInt(1, subscriberId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RenderedService renderedService = createRenderedService(rs);
                renderedServices.add(renderedService);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return renderedServices;
    }

    public void save(RenderedService renderedService) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"rendered_service\" (\"subscriber_id\", \"service_id\", \"date\",  \"price\") VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, renderedService.getSubscriberId());
            preparedStatement.setInt(2, renderedService.getServiceId());
            preparedStatement.setDate(3, Date.valueOf(renderedService.getDate()));
            preparedStatement.setInt(4, renderedService.getPrice());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(RenderedService renderedService) {
        if(getRenderedService(renderedService.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"rendered_service\" set \"subscriber_id\" = ?, \"service_id\" = ?, \"date\" = ?, \"price\" = ? where \"id\" = ?");
                preparedStatement.setInt(1, renderedService.getSubscriberId());
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
        if(getRenderedService(id) != null) {
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

    private RenderedService createRenderedService(ResultSet rs) throws SQLException{
        RenderedService renderedService = new RenderedService();
        renderedService.setId(rs.getInt("id"));
        renderedService.setServiceId(rs.getInt("service_id"));
        renderedService.setSubscriberId(rs.getInt("subscriber_id"));
        renderedService.setPrice(rs.getInt("price"));
        renderedService.setDate(rs.getDate("date").toLocalDate());
        return renderedService;
    }
}
