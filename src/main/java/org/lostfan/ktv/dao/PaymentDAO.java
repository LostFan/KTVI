package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.Subscriber;

public interface PaymentDAO {

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByDate(LocalDate date);

    List<Payment> getPaymentsBySubscriber(int subscriberId);

    void save(Payment tariff);

    void update(Payment tariff);

    void delete(int id);
}
