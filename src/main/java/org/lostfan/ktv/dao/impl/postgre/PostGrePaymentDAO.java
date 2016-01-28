package org.lostfan.ktv.dao.impl.postgre;

import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.PaymentType;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostGrePaymentDAO implements PaymentDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\"  order by \"date\" limit 100");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                payments.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    public List<Payment> getByMonth(LocalDate date) {
        List<Payment> payments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\" where \"date\" >= ? AND \"date\" < ?  order by \"date\" ");
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setDate(2, Date.valueOf(date.plusMonths(1)));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                payments.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    public Payment get(int id) {
        Payment payment = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                payment = constructEntity(rs);

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
                payments.add(constructEntity(rs));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    public List<Payment> getPaymentsBySubscriberId(int paymentId) {
        List<Payment> payments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\" where \"subscriber_account\" = ?");
            preparedStatement.setInt(1, paymentId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                payments.add(constructEntity(rs));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    public void save(Payment payment) {
        try {
            PreparedStatement preparedStatement;
            if(payment.getId() != null) {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"payment\" (\"subscriber_account\", \"service_id\", \"rendered_service_id\", \"price\", \"date\", \"bank_file_name\", \"id\")" +
                                " VALUES(?, ?, ?, ?, ?, ?, ?); " +
//                                "ALTER SEQUENCE serial_payment RESTART WITH ?;");
                                "");
                preparedStatement.setInt(7, payment.getId());
//                preparedStatement.setInt(7, payment.getId() + 1);
            } else {
                preparedStatement = getConnection().prepareStatement(
                        "INSERT INTO \"payment\" (\"subscriber_account\", \"service_id\", \"rendered_service_id\", \"price\", \"date\", \"bank_file_name\")" +
                                " VALUES(?, ?, ?, ?, ?, ?)");
            }
            preparedStatement.setInt(1, payment.getSubscriberAccount());
            preparedStatement.setInt(2, payment.getServicePaymentId());
            if(payment.getRenderedServicePaymentId() != null) {
                preparedStatement.setInt(3, payment.getRenderedServicePaymentId());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }
            preparedStatement.setInt(4, payment.getPrice());
            preparedStatement.setDate(5, Date.valueOf(payment.getDate()));
            preparedStatement.setString(6, payment.getBankFileName());
            preparedStatement.executeUpdate();
            if(payment.getId() != null) {
                return;
            }
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT lastval()");
            resultSet.next();
            payment.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Payment payment) {
        if(get(payment.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"payment\" set \"subscriber_account\" = ?, \"service_id\" = ?, \"rendered_service_id\" = ?, \"payment_type_id\" = ?, \"price\" = ?, \"date\" = ?, \"bank_file_name\" where \"id\" = ?");
                preparedStatement.setInt(1, payment.getSubscriberAccount());
                preparedStatement.setInt(2, payment.getServicePaymentId());
                if( payment.getRenderedServicePaymentId() != null) {
                    preparedStatement.setInt(3, payment.getRenderedServicePaymentId());
                }
                if( payment.getPaymentTypeId() != null) {
                    preparedStatement.setInt(4, payment.getPaymentTypeId());
                }
                preparedStatement.setInt(5, payment.getPrice());
                preparedStatement.setDate(6, Date.valueOf(payment.getDate()));
                preparedStatement.setString(7, payment.getBankFileName());
                preparedStatement.setInt(8, payment.getId());
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

    public List<Payment> getPaymentsByBankFileName(String bankFileName) {
        List<Payment> payments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"payment\" where \"bank_file_name\" = ?");
            preparedStatement.setString(1, bankFileName);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                payments.add(constructEntity(rs));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return payments;
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

    @Override
    public List<Payment> getAllContainsInName(String str) {
        List<Payment> payments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"rendered_service\" where LOWER(\"id\" AS varchar(10)) LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                payments.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return payments;
    }

    private Payment constructEntity(ResultSet rs) throws SQLException{
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setPrice(rs.getInt("price"));
        payment.setDate(rs.getDate("date").toLocalDate());
        if(rs.getObject("rendered_service_id") != null) {
            payment.setPaymentTypeId(rs.getInt("payment_type_id"));
        }
        payment.setSubscriberAccount(rs.getInt("subscriber_account"));
        if(rs.getObject("rendered_service_id") != null) {
            payment.setRenderedServicePaymentId(rs.getInt("rendered_service_id"));
        }
        payment.setServicePaymentId(rs.getInt("service_id"));
        payment.setBankFileName(rs.getString("bank_file_name"));
        return payment;
    }
}
