package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DailyRegisterReport {

    private List<PaymentExt> payments;

    private Map<Service, Double> serviceAmounts;

    private BigDecimal overallSum;

    public List<PaymentExt> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentExt> payments) {
        this.payments = payments;
    }

    public Map<Service, Double> getServiceAmounts() {
        return serviceAmounts;
    }

    public void setServiceAmounts(Map<Service, Double> serviceAmounts) {
        this.serviceAmounts = serviceAmounts;
    }

    public BigDecimal getOverallSum() {
        return overallSum;
    }

    public void setOverallSum(BigDecimal overallSum) {
        this.overallSum = overallSum;
    }
}
