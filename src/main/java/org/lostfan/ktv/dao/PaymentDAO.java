package org.lostfan.ktv.dao;

import java.math.BigDecimal;
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

    List<Payment> getList(Integer subscriberId, LocalDate date, String bankFileName);

    Map<Integer, BigDecimal> getAllPaymentsPriceInMonthForSubscriberByServiceId(int serviceId, LocalDate date);

    Map<Integer, BigDecimal> getAllPaymentsPriceBeforeDate(LocalDate date);

    Map<Integer, BigDecimal> getAllPaymentsPriceForSubscriberToDate(int serviceId, LocalDate date);

    Map<Integer, Payment> getForNotClosedRenderedServices(Integer subscriberAccount, Integer serviceId);

    Map<Integer, List<Payment>> getServiceAndSubscriberPaymentMap();

    void deleteByDate(LocalDate date);
}
