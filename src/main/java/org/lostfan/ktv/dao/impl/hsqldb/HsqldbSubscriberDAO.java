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

    public int getBalanceByDay(int subscriberId, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getTariffByDay(int subscriberId, LocalDate date) {
        if(getSubscriber(subscriberId) != null) {
            int tariffId = 0;
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT TOP 1 * FROM \"service_price\" where \"service_id\" = ? AND \"date\" <= ?" +
                        " ORDER BY \"date\" DESC");
                preparedStatement.setInt(1, subscriberId);
                preparedStatement.setDate(2, Date.valueOf(date));
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    tariffId =  rs.getInt("tariffId");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if(tariffId == 0) {
                throw new UnsupportedOperationException("Tariff not exist for this day");
            }
            return tariffId;
        } else {
            throw new UnsupportedOperationException("Subscriber not exist");
        }
    }

    public List<SubscriberSession> getSubscriberSessions(int subscriberId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<SubscriberTariff> getSubscriberTariffs(int subscriberId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public SubscriberSession getSubscriberSession(int subscriberSessionId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void saveSubscriberSession(SubscriberSession subscriberSession) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateSubscriberSession(SubscriberSession subscriberSession) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public SubscriberTariff getSubscriberTariff(int subscriberTariffId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void saveSubscriberTariff(SubscriberTariff subscriberTariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateSubscriberTariff(SubscriberTariff subscriberTariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
