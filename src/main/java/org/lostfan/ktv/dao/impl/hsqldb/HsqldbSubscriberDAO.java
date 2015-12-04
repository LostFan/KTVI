package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.SubscriberSession;
import org.lostfan.ktv.domain.SubscriberTariff;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HsqldbSubscriberDAO implements SubscriberDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public List<Subscriber> getAll() {
        List<Subscriber> subscribers = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"subscriber\"");
            while (rs.next()) {
                Subscriber subscriber = constructEntity(rs);
                subscribers.add(subscriber);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscribers;
    }

    public Subscriber get(int id) {
        Subscriber subscriber = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber\" where \"account\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                subscriber = constructEntity(rs);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscriber;
    }

    public void save(Subscriber subscriber) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"subscriber\" (\"account\", \"name\", \"street_id\", \"balance\"," +
                            " \"connected\", \"house\", \"building\", \"postcode\", \"phone\"," +
                            " \"passport_number\", \"passport_authority\", \"passport_date\") " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setInt(1, subscriber.getAccount());
            preparedStatement.setString(2, subscriber.getName());
            if(subscriber.getStreetId() != null) {
                preparedStatement.setInt(3, subscriber.getStreetId());
            }
            if(subscriber.getBalance() != null) {
                preparedStatement.setInt(4, subscriber.getBalance());
            } else {
                preparedStatement.setInt(4, 0);
            }
            if(subscriber.isConnected() != null) {
                preparedStatement.setBoolean(5, subscriber.isConnected());
            }
            if(subscriber.getHouse() != null) {
                preparedStatement.setString(6, subscriber.getHouse());
            }
            if(subscriber.getBuilding() != null) {
                preparedStatement.setString(7, subscriber.getBuilding());
            }
            if(subscriber.getPostcode() != null) {
                preparedStatement.setString(8, subscriber.getPostcode());
            }
            if(subscriber.getPhone() != null) {
                preparedStatement.setString(9, subscriber.getPhone());
            }
            if(subscriber.getPassportNumber() != null) {
                preparedStatement.setString(10, subscriber.getPassportNumber());
            }
            if(subscriber.getPassportAuthority() != null) {
                preparedStatement.setString(11, subscriber.getPassportAuthority());
            }
            if (subscriber.getPassportDate() != null) {
                preparedStatement.setDate(12, Date.valueOf(subscriber.getPassportDate()));
            }
            preparedStatement.executeUpdate();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CALL IDENTITY()");
            resultSet.next();
            subscriber.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Subscriber subscriber) {
        if(get(subscriber.getAccount()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"subscriber\" set " +
                                "\"name\" = ?, " +
                                "\"street_id\" = ?, " +
                                "\"balance\" = ?, " +
                                "\"connected\" = ?, " +
                                "\"house\" = ?, " +
                                "\"building\" = ?, " +
                                "\"postcode\" = ?, " +
                                "\"phone\" = ?, " +
                                "\"passport_number\" = ?, " +
                                "\"passport_authority\" = ?, " +
                                "\"passport_date\" = ? " +
                                "where \"account\" = ?");
                preparedStatement.setString(1, subscriber.getName());
                if(subscriber.getStreetId() != null) {
                    preparedStatement.setInt(2, subscriber.getStreetId());
                }
                if(subscriber.getBalance() != null) {
                    preparedStatement.setInt(3, subscriber.getBalance());
                } else {
                    preparedStatement.setInt(3, 0);
                }
                if(subscriber.isConnected() != null) {
                    preparedStatement.setBoolean(4, subscriber.isConnected());
                }
                if(subscriber.getHouse() != null) {
                    preparedStatement.setString(5, subscriber.getHouse());
                }
                if(subscriber.getBuilding() != null) {
                    preparedStatement.setString(6, subscriber.getBuilding());
                }
                if(subscriber.getPostcode() != null) {
                    preparedStatement.setString(7, subscriber.getPostcode());
                }
                if(subscriber.getPhone() != null) {
                    preparedStatement.setString(8, subscriber.getPhone());
                }
                if(subscriber.getPassportNumber() != null) {
                    preparedStatement.setString(9, subscriber.getPassportNumber());
                }
                if(subscriber.getPassportAuthority() != null) {
                    preparedStatement.setString(10, subscriber.getPassportAuthority());
                }
                if (subscriber.getPassportDate() != null) {
                    preparedStatement.setDate(11, Date.valueOf(subscriber.getPassportDate()));
                }
                preparedStatement.setInt(12, subscriber.getAccount());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Update nonexistent element");
        }
    }

    public void delete(int subscriberAccount) {
        if(get(subscriberAccount) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"subscriber\" where \"account\" = ?");
                preparedStatement.setInt(1, subscriberAccount);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Delete nonexistent element");
        }
    }

    public int getBalanceByDate(int subscriberId, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Integer getTariffIdByDate(int subscriberAccount, LocalDate date) {
        if(get(subscriberAccount) != null) {
            Integer tariffId = null;
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT TOP 1 * FROM \"subscriber_tariff\" where \"subscriber_account\" = ? AND \"connection_date\" <= ?" +
                        " AND (\"disconnection_date\" IS NULL OR \"disconnection_date\" > ?) ORDER BY \"connection_date\" DESC");
                preparedStatement.setInt(1, subscriberAccount);
                preparedStatement.setDate(2, Date.valueOf(date));
                preparedStatement.setDate(3, Date.valueOf(date));
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    tariffId =  rs.getInt("tariff_id");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return tariffId;
        } else {
            throw new IllegalArgumentException("Subscriber not exist");
        }
    }

    public Integer getSessionIdByDate(int subscriberAccount, LocalDate date) {
        if(get(subscriberAccount) != null) {
            Integer sessionId = null;
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT TOP 1 * FROM \"subscriber_session\" where \"subscriber_account\" = ? AND \"connection_date\" <= ?" +
                        " AND (\"disconnection_date\" IS NULL OR \"disconnection_date\" > ?) ORDER BY \"connection_date\" DESC");
                preparedStatement.setInt(1, subscriberAccount);
                preparedStatement.setDate(2, Date.valueOf(date));
                preparedStatement.setDate(3, Date.valueOf(date));
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    sessionId =  rs.getInt("id");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return sessionId;
        } else {
            throw new IllegalArgumentException("Subscriber not exist");
        }
    }

    public List<SubscriberSession> getSubscriberSessions(int subscriberAccount) {
        List<SubscriberSession> subscriberSessions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_session\" where \"subscriber_account\" = ?");
            preparedStatement.setInt(1, subscriberAccount);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SubscriberSession subscriberSession = new SubscriberSession();
                subscriberSession.setSubscriberAccount(rs.getInt("subscriber_account"));
                subscriberSession.setConnectionDate(rs.getDate("connection_date").toLocalDate());
                if(rs.getDate("disconnection_date") != null) {
                    subscriberSession.setDisconnectionDate(rs.getDate("disconnection_date").toLocalDate());
                }
                subscriberSessions.add(subscriberSession);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscriberSessions;
    }

    public List<SubscriberTariff> getSubscriberTariffs(int subscriberAccount) {
        List<SubscriberTariff> subscriberTariffs = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_tariff\" where \"subscriber_account\" = ?");
            preparedStatement.setInt(1, subscriberAccount);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SubscriberTariff subscriberTariff = new SubscriberTariff();
                subscriberTariff.setSubscriberAccount(rs.getInt("subscriber_account"));
                subscriberTariff.setConnectTariff(rs.getDate("connection_date").toLocalDate());
                if(rs.getDate("disconnection_date") != null) {
                    subscriberTariff.setDisconnectTariff(rs.getDate("disconnection_date").toLocalDate());
                }
                subscriberTariffs.add(subscriberTariff);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscriberTariffs;
    }

    public SubscriberSession getSubscriberSession(Integer subscriberId, LocalDate localDate) {
        SubscriberSession subscriberSession = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_session\" where \"subscriber_account\" = ? AND \"connection_date\" = ?");
            preparedStatement.setInt(1, subscriberId);
            preparedStatement.setDate(2, Date.valueOf(localDate));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                subscriberSession = new SubscriberSession();
                subscriberSession.setSubscriberAccount(rs.getInt("subscriber_account"));
                subscriberSession.setConnectionDate(rs.getDate("connection_date").toLocalDate());
                if(rs.getDate("disconnection_date") != null) {
                    subscriberSession.setDisconnectionDate(rs.getDate("disconnection_date").toLocalDate());
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscriberSession;
    }

    public void saveSubscriberSession(SubscriberSession subscriberSession) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"subscriber_session\" (\"subscriber_account\", \"connection_date\", \"disconnection_date\") VALUES(?, ?, ?)");
            preparedStatement.setInt(1, subscriberSession.getSubscriberAccount());
            preparedStatement.setDate(2, Date.valueOf(subscriberSession.getConnectionDate()));
            if (subscriberSession.getDisconnectionDate() != null) {
                preparedStatement.setDate(3, Date.valueOf(subscriberSession.getDisconnectionDate()));
            } else {
                preparedStatement.setDate(3, null);
            }
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveSubscriberSession(int subscriberId, LocalDate date) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"subscriber_session\" (\"subscriber_account\", \"connection_date\") VALUES(?, ?)");
            preparedStatement.setInt(1, subscriberId);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateSubscriberSession(SubscriberSession subscriberSession) {
        if(getSubscriberSession(subscriberSession.getSubscriberAccount(), subscriberSession.getConnectionDate()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"subscriber_session\" set \"disconnection_date\" = ?  where \"subscriber_account\" = ? AND \"connection_date\" = ?");

                if (subscriberSession.getDisconnectionDate() != null) {
                    preparedStatement.setDate(1, Date.valueOf(subscriberSession.getDisconnectionDate()));
                } else {
                    preparedStatement.setDate(1, null);
                }
                preparedStatement.setInt(2, subscriberSession.getSubscriberAccount());
                preparedStatement.setDate(3, Date.valueOf(subscriberSession.getConnectionDate()));
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    @Override
    public void deleteSubscriberSession(Integer subscriberId, LocalDate localDate) {
        if(getSubscriberSession(subscriberId, localDate) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"subscriber_session\" where \"subscriber_account\" = ? AND \"connection_date\" = ?");
                preparedStatement.setInt(1, subscriberId);
                preparedStatement.setDate(2, Date.valueOf(localDate));
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Delete nonexistent element");
        }
    }

    public SubscriberTariff getSubscriberTariff(Integer subscriberId, LocalDate localDate) {
        SubscriberTariff subscriberTariff = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_tariff\" where \"subscriber_account\" = ? AND \"connection_date\" = ?");
            preparedStatement.setInt(1, subscriberId);
            preparedStatement.setDate(2, Date.valueOf(localDate));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                subscriberTariff = new SubscriberTariff();
                subscriberTariff.setSubscriberAccount(rs.getInt("subscriber_account"));
                subscriberTariff.setConnectTariff(rs.getDate("connection_date").toLocalDate());
                if(rs.getDate("disconnection_date") != null) {
                    subscriberTariff.setDisconnectTariff(rs.getDate("disconnection_date").toLocalDate());
                }
                subscriberTariff.setTariffId(rs.getInt("tariff_id"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscriberTariff;
    }

    public void saveSubscriberTariff(SubscriberTariff subscriberTariff) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"subscriber_tariff\" (\"subscriber_account\", \"connection_date\", \"disconnection_date\", \"tariff_id\") VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, subscriberTariff.getSubscriberAccount());
            preparedStatement.setDate(2, Date.valueOf(subscriberTariff.getConnectTariff()));
            if (subscriberTariff.getDisconnectTariff() != null) {
                preparedStatement.setDate(3, Date.valueOf(subscriberTariff.getDisconnectTariff()));
            } else {
                preparedStatement.setDate(3, null);
            }
            preparedStatement.setInt(4, subscriberTariff.getTariffId());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateSubscriberTariff(SubscriberTariff subscriberTariff) {
        if(getSubscriberTariff(subscriberTariff.getSubscriberAccount(), subscriberTariff.getConnectTariff()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"subscriber_tariff\" set  \"disconnection_date\" = ?, \"tariff_id\" = ?  where \"subscriber_account\" = ? AND \"connection_date\" = ?");

                if (subscriberTariff.getDisconnectTariff() != null) {
                    preparedStatement.setDate(1, Date.valueOf(subscriberTariff.getDisconnectTariff()));
                } else {
                    preparedStatement.setDate(1, null);
                }
                preparedStatement.setInt(2, subscriberTariff.getTariffId());
                preparedStatement.setInt(3, subscriberTariff.getSubscriberAccount());
                preparedStatement.setDate(4, Date.valueOf(subscriberTariff.getConnectTariff()));
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    @Override
    public void deleteSubscriberTariff(Integer subscriberId, LocalDate localDate) {
        if(getSubscriberTariff(subscriberId, localDate) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"subscriber_tariff\" where \"subscriber_account\" = ? AND \"connection_date\" = ?");
                preparedStatement.setInt(1, subscriberId);
                preparedStatement.setDate(2, Date.valueOf(localDate));
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Delete nonexistent element");
        }
    }

    public List<Subscriber> getSubscribersByBeginningPartOfName(String str) {
        List<Subscriber> subscribers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, (str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Subscriber subscriber = constructEntity(rs);
                subscribers.add(subscriber);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscribers;
    }

    public List<Subscriber> getSubscribersByBeginningPartOfAccount(String str) {
        List<Subscriber> subscribers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"account\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, (str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Subscriber subscriber = constructEntity(rs);
                subscribers.add(subscriber);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscribers;
    }

    @Override
    public List<Subscriber> getAllContainsInName(String str) {
        List<Subscriber> subscribers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber\" where LOWER(\"name\") LIKE ?");
            preparedStatement.setString(1, ("%" + str + "%").toLowerCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                subscribers.add(constructEntity(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscribers;
    }

    private Subscriber constructEntity(ResultSet rs) throws SQLException{
        Subscriber subscriber = new Subscriber();
        subscriber.setAccount(rs.getInt("account"));
        subscriber.setName(rs.getString("name"));
        if(rs.getObject("street_id") != null) {
            subscriber.setStreetId(rs.getInt("street_id"));
        }
        subscriber.setBalance(rs.getInt("balance"));
        subscriber.setConnected(rs.getBoolean("connected"));
        subscriber.setHouse(rs.getString("house"));
        subscriber.setBuilding(rs.getString("building"));
        subscriber.setPostcode(rs.getString("postcode"));
        subscriber.setPhone(rs.getString("phone"));
        subscriber.setPassportNumber(rs.getString("passport_number"));
        subscriber.setPassportAuthority(rs.getString("passport_authority"));
        Date passportDate = rs.getDate("passport_date");
        if (passportDate != null) {
            subscriber.setPassportDate(passportDate.toLocalDate());
        }
        return subscriber;
    }
}
