package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.PaymentType;
import org.lostfan.ktv.domain.Subscriber;

public interface PaymentDAO extends EntityDAO<Payment> {

    List<Payment> getAll();

    Payment get(int id);

    List<Payment> getPaymentsByDate(LocalDate date);

    List<Payment> getPaymentsBySubscriberId(int subscriberId);

    void save(Payment payment);

    void update(Payment payment);

    void delete(int id);

    List<PaymentType> getAllPaymentTypes();

    PaymentType getPaymentType(int id);

    void savePaymentType(PaymentType paymentType);

    void updatePaymentType(PaymentType paymentType);

    void deletePaymentType(int paymentTypeId);
}
