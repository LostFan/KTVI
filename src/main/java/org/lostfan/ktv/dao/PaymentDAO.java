package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.Subscriber;

public interface PaymentDAO {

    public List<Payment> getAllPayments();

    public List<Payment> getPaymentsByDate(LocalDate date);

    public List<Payment> getPaymentsBySubscriber(int subscriberId);

    public void save(Payment tariff);

    public void update(Payment tariff);

    public void delete(int id);
}
