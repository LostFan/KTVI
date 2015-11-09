package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.MaterialConsumptionDAO;
import org.lostfan.ktv.dao.MaterialDAO;
import org.lostfan.ktv.domain.Material;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HsqldbMaterialConsumptionDAO implements MaterialConsumptionDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<MaterialConsumption> getAll() {
        List<MaterialConsumption> materialConsumptions = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"material_consumption\"");
            while (rs.next()) {
                MaterialConsumption materialConsumption = new MaterialConsumption();
                materialConsumption.setId(rs.getInt("id"));
                materialConsumption.setMaterialId(rs.getInt("material_id"));
                materialConsumption.setRenderedServiceId(rs.getInt("rendered_service_id"));
                materialConsumption.setAmount(rs.getDouble("amount"));
                materialConsumptions.add(materialConsumption);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return materialConsumptions;
    }

    public MaterialConsumption get(int id) {
        MaterialConsumption materialConsumption = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"material_consumption\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                materialConsumption = new MaterialConsumption();
                materialConsumption.setId(rs.getInt("id"));
                materialConsumption.setMaterialId(rs.getInt("material_id"));
                materialConsumption.setRenderedServiceId(rs.getInt("rendered_service_id"));
                materialConsumption.setAmount(rs.getDouble("amount"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return materialConsumption;
    }

    public void save(MaterialConsumption materialConsumption) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"material_consumption\" (\"material_id\", \"rendered_service_id\", \"amount\") VALUES(?, ?, ?)");
            preparedStatement.setInt(1, materialConsumption.getMaterialId());
            preparedStatement.setInt(2, materialConsumption.getRenderedServiceId());
            preparedStatement.setDouble(3, materialConsumption.getAmount());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(MaterialConsumption materialConsumption) {
        if(get(materialConsumption.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"material_consumption\" set \"material_id\" = ?, \"rendered_service_id\" = ?, \"amount\" = ? where \"id\" = ?");
                preparedStatement.setInt(1, materialConsumption.getMaterialId());
                preparedStatement.setInt(2, materialConsumption.getRenderedServiceId());
                preparedStatement.setDouble(3, materialConsumption.getAmount());
                preparedStatement.setInt(4, materialConsumption.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int materialConsumptionId) {
        if(get(materialConsumptionId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"material_consumption\" where \"id\" = ?");
                preparedStatement.setInt(1, materialConsumptionId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public List<MaterialConsumption> getMaterialConsumptionsByRenderedServiceId(int renderedServiceId) {
        List<MaterialConsumption> materialConsumptions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"material_consumption\" WHERE \"rendered_service_id\" = ?");
            preparedStatement.setInt(1, renderedServiceId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                MaterialConsumption materialConsumption = new MaterialConsumption();
                materialConsumption.setId(rs.getInt("id"));
                materialConsumption.setMaterialId(rs.getInt("material_id"));
                materialConsumption.setRenderedServiceId(rs.getInt("rendered_service_id"));
                materialConsumption.setAmount(rs.getDouble("amount"));
                materialConsumptions.add(materialConsumption);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return materialConsumptions;
    }
}