package dao;

import java.time.LocalDate;
import java.util.List;

import domain.Subscriber;

public interface SubscriberDAO {

    public List<Subscriber> getAllSubscribers();

    public Subscriber getSubscriber(int id);

    public void save(Subscriber subscriber);

    public void update(Subscriber subscriber);

    public void delete(Subscriber subscriber);

    public int getBalanceByDay(Subscriber subscriber, LocalDate date);

    public int getTariffByDay(Subscriber subscriber, LocalDate date);
}
