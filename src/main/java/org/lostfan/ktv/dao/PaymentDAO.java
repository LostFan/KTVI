package org.lostfan.ktv.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.PaymentType;

public interface PaymentDAO extends EntityDAO<Payment> {

    List<Payment> getByMonth(LocalDate date);

    List<Payment> getByDate(LocalDate date);

    List<Payment> getBySubscriber(int subscriberAccount);

    List<Payment> getByBankFileName(String bankFileName);

    Map<Integer, Integer> getAllPaymentsPriceInMonthForSubscriberByServiceId(int serviceId, LocalDate date);

    Map<Integer, Integer> getAllPaymentsPriceForSubscriberToDate(int serviceId, LocalDate date);

    Map<Integer, Payment> getForNotClosedRenderedServices(Integer subscriberAccount, Integer serviceId);

    List<PaymentType> getAllPaymentTypes();

    PaymentType getPaymentType(int paymentTypeId);

    void savePaymentType(PaymentType paymentType);

    void updatePaymentType(PaymentType paymentType);

    void deletePaymentType(int paymentTypeId);
}
