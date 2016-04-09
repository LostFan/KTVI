package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Service;

import java.util.List;
import java.util.Map;

public class DailyRegisterReport {

    private List<PaymentExt> payments;

    private Map<Service, Integer> serviceAmounts;

    private int overallSum;

    public List<PaymentExt> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentExt> payments) {
        this.payments = payments;
    }

    public Map<Service, Integer> getServiceAmounts() {
        return serviceAmounts;
    }

    public void setServiceAmounts(Map<Service, Integer> serviceAmounts) {
        this.serviceAmounts = serviceAmounts;
    }

    public int getOverallSum() {
        return overallSum;
    }

    public void setOverallSum(int overallSum) {
        this.overallSum = overallSum;
    }
}
