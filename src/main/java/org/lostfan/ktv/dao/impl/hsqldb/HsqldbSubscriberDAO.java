package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.dao.TariffDAO;
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

    public List<Subscriber> getAllSubscribers() {
        List<Subscriber> subscribers = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"subscriber\"");
            while (rs.next()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setId(rs.getInt("id"));
                subscriber.setName(rs.getString("name"));
                subscriber.setAccount(rs.getInt("account"));

                subscribers.add(subscriber);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscribers;
    }

    public Subscriber getSubscriber(int id) {
        Subscriber subscriber = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber\" where \"id\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                subscriber = new Subscriber();
                subscriber.setId(rs.getInt("id"));
                subscriber.setName(rs.getString("name"));
                subscriber.setAccount(rs.getInt("account"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return subscriber;
    }

    public void save(Subscriber subscriber) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "INSERT INTO \"subscriber\" (\"name\", \"account\") VALUES(?, ?)");
            preparedStatement.setString(1, subscriber.getName());
            preparedStatement.setInt(2, subscriber.getAccount());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Subscriber subscriber) {
        if(getSubscriber(subscriber.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"subscriber\" set \"name\" = ?, \"account\" = ? where \"id\" = ?");
                preparedStatement.setString(1, subscriber.getName());
                preparedStatement.setInt(2, subscriber.getAccount());
                preparedStatement.setInt(3, subscriber.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public void delete(int subscriberId) {
        if(getSubscriber(subscriberId) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "DELETE FROM  \"subscriber\" where \"id\" = ?");
                preparedStatement.setInt(1, subscriberId);
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Delete nonexistent element");
        }
    }

    public int getBalanceByDate(int subscriberId, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Integer getTariffIdByDate(int subscriberId, LocalDate date) {
        if(getSubscriber(subscriberId) != null) {
            Integer tariffId = null;
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT TOP 1 * FROM \"subscriber_tariff\" where \"subscriber_id\" = ? AND \"connection_date\" <= ?" +
                        " AND (\"disconnection_date\" IS NULL OR \"disconnection_date\" > ?) ORDER BY \"connection_date\" DESC");
                preparedStatement.setInt(1, subscriberId);
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
            throw new UnsupportedOperationException("Subscriber not exist");
        }
    }

    public Integer getSessionIdByDate(int subscriberId, LocalDate date) {
        if(getSubscriber(subscriberId) != null) {
            Integer sessionId = null;
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT TOP 1 * FROM \"subscriber_session\" where \"subscriber_id\" = ? AND \"connection_date\" <= ?" +
                        " AND (\"disconnection_date\" IS NULL OR \"disconnection_date\" > ?) ORDER BY \"connection_date\" DESC");
                preparedStatement.setInt(1, subscriberId);
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
            throw new UnsupportedOperationException("Subscriber not exist");
        }
    }

    public List<SubscriberSession> getSubscriberSessions(int subscriberId) {
        List<SubscriberSession> subscriberSessions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_session\" where \"subscriber_id\" = ?");
            preparedStatement.setInt(1, subscriberId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SubscriberSession subscriberSession = new SubscriberSession();
                subscriberSession.setId(rs.getInt("id"));
                subscriberSession.setSubscriberId(rs.getInt("subscriber_id"));
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

    public List<SubscriberTariff> getSubscriberTariffs(int subscriberId) {
        List<SubscriberTariff> subscriberTariffs = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_tariff\" where \"subscriber_id\" = ?");
            preparedStatement.setInt(1, subscriberId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SubscriberTariff subscriberTariff = new SubscriberTariff();
                subscriberTariff.setId(rs.getInt("id"));
                subscriberTariff.setSubscriberId(rs.getInt("subscriber_id"));
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

    public SubscriberSession getSubscriberSession(int subscriberSessionId) {
        SubscriberSession subscriberSession = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_session\" where \"id\" = ?");
            preparedStatement.setInt(1, subscriberSessionId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                subscriberSession = new SubscriberSession();
                subscriberSession.setId(rs.getInt("id"));
                subscriberSession.setSubscriberId(rs.getInt("subscriber_id"));
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
                    "INSERT INTO \"subscriber_session\" (\"subscriber_id\", \"connection_date\", \"disconnection_date\") VALUES(?, ?, ?)");
            preparedStatement.setInt(1, subscriberSession.getSubscriberId());
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

    public void updateSubscriberSession(SubscriberSession subscriberSession) {
        if(getSubscriberSession(subscriberSession.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"subscriber_session\" set \"subscriber_id\" = ?, \"connection_date\" = ?, \"disconnection_date\" = ?  where \"id\" = ?");
                preparedStatement.setInt(1, subscriberSession.getSubscriberId());
                preparedStatement.setDate(2, Date.valueOf(subscriberSession.getConnectionDate()));
                if (subscriberSession.getDisconnectionDate() != null) {
                    preparedStatement.setDate(3, Date.valueOf(subscriberSession.getDisconnectionDate()));
                } else {
                    preparedStatement.setDate(3, null);
                }
                preparedStatement.setInt(4, subscriberSession.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }

    public SubscriberTariff getSubscriberTariff(int subscriberTariffId) {
        SubscriberTariff subscriberTariff = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM \"subscriber_tariff\" where \"id\" = ?");
            preparedStatement.setInt(1, subscriberTariffId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                subscriberTariff = new SubscriberTariff();
                subscriberTariff.setId(rs.getInt("id"));
                subscriberTariff.setSubscriberId(rs.getInt("subscriber_id"));
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
                    "INSERT INTO \"subscriber_tariff\" (\"subscriber_id\", \"connection_date\", \"disconnection_date\", \"tariff_id\") VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, subscriberTariff.getSubscriberId());
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
        if(getSubscriberTariff(subscriberTariff.getId()) != null) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "UPDATE \"subscriber_tariff\" set \"subscriber_id\" = ?, \"connection_date\" = ?, \"disconnection_date\" = ?, \"tariff_id\" = ?  where \"id\" = ?");
                preparedStatement.setInt(1, subscriberTariff.getSubscriberId());
                preparedStatement.setDate(2, Date.valueOf(subscriberTariff.getConnectTariff()));
                if (subscriberTariff.getDisconnectTariff() != null) {
                    preparedStatement.setDate(3, Date.valueOf(subscriberTariff.getDisconnectTariff()));
                } else {
                    preparedStatement.setDate(3, null);
                }
                preparedStatement.setInt(4, subscriberTariff.getTariffId());
                preparedStatement.setInt(5, subscriberTariff.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Update nonexistent element");
        }
    }
}
