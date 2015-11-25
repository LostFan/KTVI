package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.MaterialConsumptionDAO;
import org.lostfan.ktv.dao.RenderedServiceDAO;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.dto.FullRenderedService;
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
    
    private MaterialConsumptionDAO materialConsumptionDAO = DAOFactory.getDefaultDAOFactory().getMaterialConsumptionDAO();

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

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

    public List<RenderedService> getRenderedServicesByDate(LocalDate date) {
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

    public List<RenderedService> getRenderedServicesBySubscriberId(int subscriberId) {
        List<RenderedService> renderedServices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where \"subscriber_id\" = ?");
            preparedStatement.setInt(1, subscriberId);
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

    public void save(RenderedService renderedService) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"rendered_service\" (\"subscriber_id\", \"service_id\", \"date\",  \"price\") VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, renderedService.getSubscriberId());
            preparedStatement.setInt(2, renderedService.getServiceId());
            preparedStatement.setDate(3, Date.valueOf(renderedService.getDate()));
            preparedStatement.setInt(4, renderedService.getPrice());
            preparedStatement.executeUpdate();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CALL IDENTITY()");
            resultSet.next();
            renderedService.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void saveDTO(FullRenderedService fullRenderedService) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"rendered_service\" (\"subscriber_id\", \"service_id\", \"date\",  \"price\") VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, fullRenderedService.getSubscriberId());
            preparedStatement.setInt(2, fullRenderedService.getServiceId());
            preparedStatement.setDate(3, Date.valueOf(fullRenderedService.getDate()));
            preparedStatement.setInt(4, fullRenderedService.getPrice());
            preparedStatement.executeUpdate();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CALL IDENTITY()");
            resultSet.next();
            fullRenderedService.setId(resultSet.getInt(1));
            for (MaterialConsumption materialConsumption : fullRenderedService.getMaterialConsumption()) {
                materialConsumption.setRenderedServiceId(fullRenderedService.getId());
                materialConsumptionDAO.save(materialConsumption);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateDTO(FullRenderedService fullRenderedService) {
        if(get(fullRenderedService.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"rendered_service\" set \"subscriber_id\" = ?, \"service_id\" = ?, \"date\" = ?, \"price\" = ? where \"id\" = ?");
                preparedStatement.setInt(1, fullRenderedService.getSubscriberId());
                preparedStatement.setInt(2, fullRenderedService.getServiceId());
                preparedStatement.setDate(3, Date.valueOf(fullRenderedService.getDate()));
                preparedStatement.setInt(4, fullRenderedService.getPrice());
                preparedStatement.setInt(5, fullRenderedService.getId());
                preparedStatement.executeUpdate();
                List<MaterialConsumption> materialConsumptionList = materialConsumptionDAO.getMaterialConsumptionsByRenderedServiceId(fullRenderedService.getId());
                for (MaterialConsumption materialConsumption : materialConsumptionList) {
                    if(!fullRenderedService.getMaterialConsumption().contains(materialConsumption)) {
                        materialConsumptionDAO.delete(materialConsumption.getId());
                    }
                }
                for (MaterialConsumption materialConsumption : fullRenderedService.getMaterialConsumption()) {
                    if(materialConsumption.getId() != null) {
                        materialConsumptionDAO.update(materialConsumption);
                    } else {
                        materialConsumption.setRenderedServiceId(fullRenderedService.getId());
                        materialConsumptionDAO.save(materialConsumption);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void update(RenderedService renderedService) {
        if(get(renderedService.getId()) != null) {
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
        renderedService.setSubscriberId(rs.getInt("subscriber_id"));
        renderedService.setPrice(rs.getInt("price"));
        renderedService.setDate(rs.getDate("date").toLocalDate());
        return renderedService;
    }
}
