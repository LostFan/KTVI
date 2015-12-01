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

import org.lostfan.ktv.dao.TariffPriceDAO;
import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.utils.ConnectionManager;

public class HsqldbTariffPriceDAO implements TariffPriceDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public TariffPrice getByTariffIdAndDate(int tariffId, LocalDate date) {
        TariffPrice tariffPrice = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"tariff_price\" where \"tariff_id\" = ? AND \"date\" = ?");
            preparedStatement.setInt(1, tariffId);
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                tariffPrice = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffPrice;
    }

    public TariffPrice get(int id) {
        TariffPrice tariffPrice = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"tariff_price\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                tariffPrice = constructEntity(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffPrice;
    }

    public List<TariffPrice> getAll() {
        List<TariffPrice> tariffPrices = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"tariff_price\"");
            while (rs.next()) {
                tariffPrices.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffPrices;
    }

    public void save(TariffPrice tariffPrice) {
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

    public void update(TariffPrice tariffPrice) {
        if(get(tariffPrice.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"tariff_price\" set \"tariff_id\" = ?, \"date\" = ?, \"price\" = ? where \"id\" = ?");
                preparedStatement.setInt(1, tariffPrice.getTariffId());
                preparedStatement.setDate(2, Date.valueOf(tariffPrice.getDate()));
                preparedStatement.setInt(3, tariffPrice.getPrice());
                preparedStatement.setInt(4, tariffPrice.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }


    public void deleteByTariffIdAndDate(int tariffId, LocalDate date) {

        if(getByTariffIdAndDate(tariffId, date) != null)
        {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"tariff_price\" where \"tariff_id\" = ? AND \"date\" = ?");
                preparedStatement.setInt(1, tariffId);
                preparedStatement.setDate(2, Date.valueOf(date));
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public void delete(int tariffPriceId) {
        if(get(tariffPriceId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"tariff_price\" where \"id\" = ?");
                preparedStatement.setInt(1, tariffPriceId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
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
    public List<TariffPrice> getAllContainsInName(String str) {
        List<TariffPrice> tariffPrices = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"tariff_price\" where LOWER(\"id\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                tariffPrices.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tariffPrices;
    }

    private TariffPrice constructEntity(ResultSet rs) throws SQLException{
        TariffPrice tariffPrice = new TariffPrice();
        tariffPrice.setId(rs.getInt("id"));
        tariffPrice.setTariffId(rs.getInt("tariff_id"));
        tariffPrice.setDate(rs.getDate("date").toLocalDate());
        tariffPrice.setPrice(rs.getInt("price"));
        return tariffPrice;
    }
}
