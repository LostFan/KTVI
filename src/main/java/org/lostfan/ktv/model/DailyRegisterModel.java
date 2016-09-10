package org.lostfan.ktv.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.dto.DailyRegisterReport;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.entity.BaseModel;
import org.lostfan.ktv.model.transform.PaymentTransformer;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.utils.excel.DailyRegisterExcel;

public class DailyRegisterModel extends BaseObservable implements BaseModel {

    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();

    private PaymentTransformer paymentTransformer = new PaymentTransformer();

    private DailyRegisterReport report;
    private LocalDate date;

    @Override
    public String getEntityNameKey() {
        return "dailyRegister";
    }

    @Override
    public List<EntityField> getFields() {
        return new ArrayList<>();
    }

    public LocalDate getDate() {
        return date;
    }

    public DailyRegisterReport getReport() {
        return report;
    }

    public void generate(LocalDate date) {
        this.date = date;
        List<Payment> payments = paymentDAO.getByDate(date);
        this.report = new DailyRegisterReport();
        this.report.setPayments(new ArrayList<>(payments.size()));

        Map<Integer, BigDecimal> serviceSums = new HashMap<>();
        BigDecimal overallSum = BigDecimal.ZERO;
        for (Payment payment : payments) {
            PaymentExt paymentExt = paymentTransformer.transformTo(payment);
            paymentExt.setService(serviceDAO.get(payment.getServicePaymentId()));
            paymentExt.setSubscriber(subscriberDAO.get(payment.getSubscriberAccount()));
            this.report.getPayments().add(paymentExt);

            overallSum = overallSum.add(payment.getPrice());
            BigDecimal serviceAmount = serviceSums.get(payment.getServicePaymentId());
            if (serviceAmount == null) {
                serviceAmount = BigDecimal.ZERO;
            }
            serviceAmount = serviceAmount.add(payment.getPrice());
            serviceSums.put(payment.getServicePaymentId(), serviceAmount);
        }
        // Amount for each service
        this.report.setOverallSum(overallSum);
        this.report.setServiceAmounts(
                serviceDAO.getAll().stream().collect(Collectors.toMap(Function.identity(), service -> {
                    BigDecimal sum = serviceSums.get(service.getId());
                    if (sum == null) {
                        return 0D;
                    }
                    return sum.doubleValue();
                }, Double::max, LinkedHashMap::new)));
        notifyObservers(report);
    }

    public String generateExcel() {
        DailyRegisterExcel dailyRegisterExcel = new DailyRegisterExcel();
        dailyRegisterExcel.setReport(this.report);
        dailyRegisterExcel.setDate(this.date);
        return dailyRegisterExcel.generate();
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAll();
    }
}
