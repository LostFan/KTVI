package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.MaterialDAO;
import org.lostfan.ktv.domain.Material;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HsqldbMaterialDAO implements MaterialDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"material\"");
            while (rs.next()) {
                Material material = new Material();
                material.setId(rs.getInt("id"));
                material.setPrice(rs.getInt("price"));
                material.setName(rs.getString("name"));
                material.setUnit(rs.getString("unit"));
                materials.add(material);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return materials;
    }

    public Material getMaterial(int id) {
        Material material = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"material\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                material = new Material();
                material.setId(rs.getInt("id"));
                material.setPrice(rs.getInt("price"));
                material.setName(rs.getString("name"));
                material.setUnit(rs.getString("unit"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return material;
    }

    public void save(Material material) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"material\" (\"name\", \"price\", \"unit\") VALUES(?, ?, ?)");
            preparedStatement.setString(1, material.getName());
            preparedStatement.setInt(2, material.getPrice());
            preparedStatement.setString(3, material.getUnit());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Material material) {
        if (getMaterial(material.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"material\" set \"name\" = ?, \"price\" = ?, \"unit\" = ? where \"id\" = ?");
                preparedStatement.setString(1, material.getName());
                preparedStatement.setInt(2, material.getPrice());
                preparedStatement.setString(3, material.getUnit());
                preparedStatement.setInt(4, material.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int id) {
        if(getMaterial(id) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"material\" where \"id\" = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public List<MaterialConsumption> getAllMaterialConsumptions() {
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

    public MaterialConsumption getMaterialConsumption(int id) {
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

    public void saveMaterialConsumption(MaterialConsumption materialConsumption) {
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

    public void updateMaterialConsumption(MaterialConsumption materialConsumption) {
        if(getMaterialConsumption(materialConsumption.getId()) != null) {
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

    public void deleteMaterialConsumption(int materialConsumptionId) {
        if(getMaterialConsumption(materialConsumptionId) != null) {
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
