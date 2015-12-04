package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.PaymentType;

public interface PaymentDAO extends EntityDAO<Payment> {

    List<Payment> getPaymentsByDate(LocalDate date);

    List<Payment> getPaymentsBySubscriberId(int subscriberId);

    List<PaymentType> getAllPaymentTypes();

    PaymentType getPaymentType(int id);

    void savePaymentType(PaymentType paymentType);

    void updatePaymentType(PaymentType paymentType);

    void deletePaymentType(int paymentTypeId);
}
