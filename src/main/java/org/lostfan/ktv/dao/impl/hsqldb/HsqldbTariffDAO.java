package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.TariffDAO;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;
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

public class HsqldbTariffDAO extends HsqldbBaseDao implements TariffDAO {

    public List<Tariff> getAll() {

        List<Tariff> tariffs = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"tariff\"");
            while (rs.next()) {
                tariffs.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffs;
    }

    public Tariff get(int id) {
        Tariff tariff = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"tariff\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                tariff = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariff;
    }

    public List<Tariff> getTariffsByName(String name) {
        List<Tariff> tariffs = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"tariff\" where \"name\" LIKE %?%");
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                tariffs.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffs;
    }

    public void save(Tariff tariff) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"tariff\" (\"id\", \"name\", \"channels\", \"digital\") VALUES(?, ?, ?, ?)");
            if (tariff.getId() != null) {
                preparedStatement.setInt(1, tariff.getId());
            }
            preparedStatement.setString(2, tariff.getName());
            preparedStatement.setString(3, tariff.getChannels());
            preparedStatement.setBoolean(4, tariff.isDigital());
            preparedStatement.executeUpdate();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CALL IDENTITY()");
            resultSet.next();
            tariff.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Tariff tariff) {
        if(get(tariff.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"tariff\" set \"name\" = ?, \"channels\" = ?, \"digital\" = ? where \"id\" = ?");
                preparedStatement.setString(1, tariff.getName());
                preparedStatement.setString(2, tariff.getChannels());
                preparedStatement.setBoolean(3, tariff.isDigital());
                preparedStatement.setInt(4, tariff.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int tariffId) {
        if(get(tariffId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"tariff\" where \"id\" = ?");
                preparedStatement.setInt(1, tariffId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public TariffPrice getTariffPrice(int tariffId, LocalDate date) {
        TariffPrice tariffPrice = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"tariff_price\" where \"tariff_id\" = ? AND \"date\" <= ?");
            preparedStatement.setInt(1, tariffId);
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                tariffPrice = constructPriceEntity(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffPrice;
    }

    public List<TariffPrice> getAllTariffPrices() {
        List<TariffPrice> tariffPrices = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"tariff_price\"");
            while (rs.next()) {
                tariffPrices.add(constructPriceEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffPrices;
    }

    @Override
    public List<TariffPrice> getTariffPrices(int tariffId) {
        List<TariffPrice> tariffPrices = new ArrayList<>();
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM \"tariff_price\" WHERE \"tariff_id\"=?");
            statement.setInt(1, tariffId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tariffPrices.add(constructPriceEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffPrices;
    }

    public void saveTariffPrice(TariffPrice tariffPrice) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"tariff_price\" (\"tariff_id\", \"date\", \"price\") VALUES(?, ?, ?)");
            preparedStatement.setInt(1, tariffPrice.getTariffId());
            preparedStatement.setDate(2, Date.valueOf(tariffPrice.getDate()));
            preparedStatement.setInt(3, tariffPrice.getPrice());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteTariffPrice(TariffPrice tariffPrice) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "DELETE FROM  \"tariff_price\" where \"tariff_id\" = ? AND \"date\" = ?");
            preparedStatement.setInt(1, tariffPrice.getTariffId());
            preparedStatement.setDate(2, Date.valueOf(tariffPrice.getDate()));
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Integer getPriceByDate(int tariffId, LocalDate date) {
        Integer price = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "SELECT TOP 1 * FROM \"tariff_price\" where \"tariff_id\" = ? AND \"date\" <= ? ORDER BY \"date\" DESC");
            preparedStatement.setInt(1, tariffId);
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
    public List<Tariff> getAllContainsInName(String str) {
        List<Tariff> tariffs = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"tariff\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                tariffs.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffs;
    }

    private Tariff constructEntity(ResultSet rs) throws SQLException{
        Tariff tariff = new Tariff();
        tariff.setId(rs.getInt("id"));
        tariff.setName(rs.getString("name"));
        tariff.setDigital(rs.getBoolean("digital"));
        tariff.setChannels(rs.getString("channels"));
        return tariff;
    }

    private TariffPrice constructPriceEntity(ResultSet rs) throws SQLException {
        TariffPrice tariffPrice = new TariffPrice();
        tariffPrice.setPrice(rs.getInt("price"));
        tariffPrice.setTariffId(rs.getInt("tariff_id"));
        tariffPrice.setDate(rs.getDate("date").toLocalDate());
        return tariffPrice;
    }
}
