package org.lostfan.ktv.dao.impl.postgre;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.dao.PaymentTypeDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.PaymentType;
import org.lostfan.ktv.utils.ConnectionManager;

public class PostGrePaymentTypeDAO extends PostgreBaseDao implements PaymentTypeDAO {
    
    public List<PaymentType> getAll() {
        List<PaymentType> paymentTypes = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"payment_type\"");
            while (rs.next()) {
                paymentTypes.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return paymentTypes;
    }

    public PaymentType get(int paymentTypeId) {
        PaymentType paymentType = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment_type\" where \"id\" = ?");
            preparedStatement.setInt(1, paymentTypeId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                paymentType = new PaymentType();
                paymentType.setId(rs.getInt("id"));
                paymentType.setName(rs.getString("name"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return paymentType;
    }

    public void save(PaymentType paymentType) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"payment_type\" (\"name\") VALUES(?)");
            preparedStatement.setString(1, paymentType.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(PaymentType paymentType) {
        if (get(paymentType.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"payment_type\" set \"name\" = ? where \"id\" = ?");
                preparedStatement.setString(1, paymentType.getName());
                preparedStatement.setInt(2, paymentType.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int paymentTypeId) {
        if (get(paymentTypeId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"payment_type\" where \"id\" = ?");
                preparedStatement.setInt(1, paymentTypeId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    @Override
    public List<PaymentType> getAllContainsInName(String str) {
        List<PaymentType> paymentTypes = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment_type\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                paymentTypes.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return paymentTypes;
    }

    private PaymentType constructEntity(ResultSet rs) throws SQLException {
        PaymentType paymentType = new PaymentType();
        paymentType.setId(rs.getInt("id"));
        paymentType.setName(rs.getString("name"));
        return paymentType;
    }
}
