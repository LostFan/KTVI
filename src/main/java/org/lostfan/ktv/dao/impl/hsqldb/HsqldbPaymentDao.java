package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.PaymentType;
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

public class HsqldbPaymentDAO implements PaymentDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"payment\"");
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setPrice(rs.getInt("price"));
                payment.setDate(rs.getDate("date").toLocalDate());
                payment.setPaymentTypeId(rs.getInt("payment_type_id"));
                payment.setSubscriberId(rs.getInt("subscriber_id"));
                payment.setServicePaymentId(rs.getInt("service_id"));
                payments.add(payment);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    public Payment getPayment(int id) {
        Payment payment = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setPrice(rs.getInt("price"));
                payment.setDate(rs.getDate("date").toLocalDate());
                payment.setPaymentTypeId(rs.getInt("payment_type_id"));
                payment.setSubscriberId(rs.getInt("subscriber_id"));
                payment.setServicePaymentId(rs.getInt("service_id"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payment;
    }

    public List<Payment> getPaymentsByDate(LocalDate date) {
        List<Payment> payments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\" where \"date\" = ?");
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setPrice(rs.getInt("price"));
                payment.setDate(rs.getDate("date").toLocalDate());
                payment.setPaymentTypeId(rs.getInt("payment_type_id"));
                payment.setSubscriberId(rs.getInt("subscriber_id"));
                payment.setServicePaymentId(rs.getInt("service_id"));
                payments.add(payment);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    public List<Payment> getPaymentsBySubscriberId(int paymentId) {
        List<Payment> payments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\" where \"subscriber_id\" = ?");
            preparedStatement.setInt(1, paymentId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setPrice(rs.getInt("price"));
                payment.setDate(rs.getDate("date").toLocalDate());
                payment.setPaymentTypeId(rs.getInt("payment_type_id"));
                payment.setSubscriberId(rs.getInt("subscriber_id"));
                payment.setServicePaymentId(rs.getInt("service_id"));
                payments.add(payment);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    public void save(Payment payment) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"payment\" (\"subscriber_id\", \"service_id\", \"payment_type_id\", \"price\", \"date\") VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, payment.getSubscriberId());
            preparedStatement.setInt(2, payment.getServicePaymentId());
            if( payment.getPaymentTypeId() != null) {
                preparedStatement.setInt(3, payment.getPaymentTypeId());
            }
            preparedStatement.setInt(4, payment.getPrice());
            preparedStatement.setDate(5, Date.valueOf(payment.getDate()));
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Payment payment) {
        if(getPayment(payment.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"payment\" set \"subscriber_id\" = ?, \"service_id\" = ?, \"payment_type_id\" = ?, \"price\" = ?, \"date\" = ? where \"id\" = ?");
                preparedStatement.setInt(1, payment.getSubscriberId());
                preparedStatement.setInt(2, payment.getServicePaymentId());
                preparedStatement.setInt(3, payment.getPaymentTypeId());
                preparedStatement.setInt(4, payment.getPrice());
                preparedStatement.setDate(5, Date.valueOf(payment.getDate()));
                preparedStatement.setInt(6, payment.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int id) {
        if(getPayment(id) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"payment\" where \"id\" = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public List<PaymentType> getAllPaymentTypes() {
        List<PaymentType> paymentTypes = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"payment_type\"");
            while (rs.next()) {
                PaymentType paymentType = new PaymentType();
                paymentType.setId(rs.getInt("id"));
                paymentType.setName(rs.getString("name"));
                paymentTypes.add(paymentType);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return paymentTypes;
    }

    public PaymentType getPaymentType(int id) {
        PaymentType paymentType = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment_type\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
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

    public void savePaymentType(PaymentType paymentType) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"payment_type\" (\"name\") VALUES(?)");
            preparedStatement.setString(1, paymentType.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void updatePaymentType(PaymentType paymentType) {
        if(getPaymentType(paymentType.getId()) != null) {
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

    public void deletePaymentType(int paymentTypeId) {
        if(getPaymentType(paymentTypeId) != null) {
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
}
