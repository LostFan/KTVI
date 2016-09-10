package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.DAOException;
import org.lostfan.ktv.dao.TariffDAO;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostGreTariffDAO extends PostgreBaseDao implements TariffDAO {

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
            throw new DAOException();
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
            throw new DAOException();
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
            throw new DAOException();
        }

        return tariffs;
    }

    public void save(Tariff tariff) {
        try {
            PreparedStatement preparedStatement;
            if(tariff.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"tariff\" (\"name\", \"channels\", \"digital\", \"id\") VALUES(?, ?, ?, ?)");
                preparedStatement.setInt(4, tariff.getId());
            } else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"tariff\" (\"name\", \"channels\", \"digital\") VALUES(?, ?, ?)");
            }

            preparedStatement.setString(1, tariff.getName());
            preparedStatement.setString(2, tariff.getChannels());
            preparedStatement.setBoolean(3, tariff.isDigital());
            preparedStatement.executeUpdate();
            if(tariff.getId() != null) {
                return;
            }
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT lastval()");
            resultSet.next();
            tariff.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
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
                throw new DAOException();
            }
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
                throw new DAOException();
            }
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
            throw new DAOException();
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
            throw new DAOException();
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
            throw new DAOException();
        }

        return tariffPrices;
    }

    public void saveTariffPrice(TariffPrice tariffPrice) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"tariff_price\" (\"tariff_id\", \"date\", \"price\") VALUES(?, ?, ?)");
            preparedStatement.setInt(1, tariffPrice.getTariffId());
            preparedStatement.setDate(2, Date.valueOf(tariffPrice.getDate()));
            preparedStatement.setBigDecimal(3, tariffPrice.getPrice());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
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
            throw new DAOException();
        }
    }

    public BigDecimal getPriceByDate(int tariffId, LocalDate date) {
        BigDecimal price = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "SELECT * FROM \"tariff_price\" where \"tariff_id\" = ? AND \"date\" <= ? ORDER BY \"date\" DESC LIMIT 1");
            preparedStatement.setInt(1, tariffId);
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                price = rs.getBigDecimal("price");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException();
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
            throw new DAOException();
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
        tariffPrice.setPrice(rs.getBigDecimal("price"));
        tariffPrice.setTariffId(rs.getInt("tariff_id"));
        tariffPrice.setDate(rs.getDate("date").toLocalDate());
        return tariffPrice;
    }
}
