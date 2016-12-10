package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.dto.SubscriberDebit;
import org.lostfan.ktv.model.entity.BaseModel;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.utils.excel.SubscriberDebitExcel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DebtorReportModel extends BaseObservable implements BaseModel {

    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private StreetDAO streetDAO = DAOFactory.getDefaultDAOFactory().getStreetDAO();
    private RenderedServiceDAO renderedServiceDAO = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();

    public DebtorReportModel() {
    }

    @Override
    public String getEntityNameKey() {
        return "debtorReport";
    }

    @Override
    public List<EntityField> getFields() {
        return new ArrayList<>();
    }


    public List<SubscriberDebit> getData(LocalDate date, BigDecimal sum, Integer tariffId) {

        List<SubscriberDebit> subscriberDebitsResult = new ArrayList<>();
        Map<Integer, BigDecimal> endPeriodDebit = renderedServiceDAO.getAllRenderedServicesPriceBeforeDate(LocalDate.now());
        Map<Integer, BigDecimal> endPeriodCredit = paymentDAO.getAllPaymentsPriceBeforeDate(LocalDate.now());
        List<Payment> paymentByPeriod = paymentDAO.getPaymentsByPeriodDate(date, LocalDate.now());
        List<Integer> subscribersWithPaymentByPeriod = paymentByPeriod.stream().mapToInt(e -> e.getSubscriberAccount()).boxed().collect(Collectors.toList());
        for (Map.Entry<Integer, BigDecimal> suscriberIdKeyBalanceValue : endPeriodDebit.entrySet()) {
            BigDecimal payedSum = endPeriodCredit.get(suscriberIdKeyBalanceValue.getKey());
            if (payedSum == null) {
                payedSum = BigDecimal.ZERO;
            }
            if (sum.compareTo(suscriberIdKeyBalanceValue.getValue().add(payedSum.negate())) <= 0) {
                if (tariffId == null || tariffId.equals(subscriberDAO.getTariffIdByDate(suscriberIdKeyBalanceValue.getKey(), LocalDate.now()))) {
                    if (!subscribersWithPaymentByPeriod.contains(suscriberIdKeyBalanceValue.getKey())) {
                        if (subscriberDAO.getNotClosedSubscriberSession(suscriberIdKeyBalanceValue.getKey(), LocalDate.now()) != null) {
                            SubscriberDebit subscriberDebit = new SubscriberDebit();
                            subscriberDebit.setSubscriberAccount(suscriberIdKeyBalanceValue.getKey());
                            subscriberDebit.setDebit(suscriberIdKeyBalanceValue.getValue().add(payedSum.negate()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            subscriberDebit.setSubscriber(subscriberDAO.get(subscriberDebit.getSubscriberAccount()));
                            if (subscriberDebit.getSubscriber() != null) {
                                subscriberDebit.setSubscriberStreet(streetDAO.get(subscriberDebit.getSubscriber().getStreetId()));
                            }
                            subscriberDebitsResult.add(subscriberDebit);
                        }
                    }
                }
            }
        }
        System.out.println(subscriberDebitsResult.size());
        return subscriberDebitsResult;
    }

    public String generateExcelReport(LocalDate date, BigDecimal sum, Integer tariffId) {
        SubscriberDebitExcel subscriberDebitExcel = new SubscriberDebitExcel("debtorReport");

        subscriberDebitExcel.setSubscriberDebitList(getData(date, sum, tariffId));

        return subscriberDebitExcel.generate();
    }

}
