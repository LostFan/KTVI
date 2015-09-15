package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.domain.Payment;

import java.time.LocalDate;
import java.util.List;

public class HsqldbPaymentDAO implements PaymentDAO {

    public List<Payment> getAllPayments() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Payment> getPaymentsByDate(LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Payment> getPaymentsBySubscriber(int subscriberId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void save(Payment tariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void update(Payment tariff) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void delete(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
