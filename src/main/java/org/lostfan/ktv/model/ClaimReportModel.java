package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.*;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.SubscriberSession;
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

public class ClaimReportModel {

    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private StreetDAO streetDAO = DAOFactory.getDefaultDAOFactory().getStreetDAO();
    private RenderedServiceDAO renderedServiceDAO = DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();

    public ClaimReportModel() {
    }

    public String getEntityNameKey() {
        return "claimReport";
    }

    public List<SubscriberDebit> getData(LocalDate date, BigDecimal sum) {

        List<SubscriberDebit> subscriberDebitsResult = new ArrayList<>();
        Map<Integer, BigDecimal> endPeriodDebit = renderedServiceDAO.getAllRenderedServicesPriceBeforeDate(LocalDate.now());
        Map<Integer, BigDecimal> endPeriodCredit = paymentDAO.getAllPaymentsPriceBeforeDate(LocalDate.now());

        for (Map.Entry<Integer, BigDecimal> subscriberIdKeyBalanceValue : endPeriodDebit.entrySet()) {
            BigDecimal payedSum = endPeriodCredit.get(subscriberIdKeyBalanceValue.getKey());
            if (payedSum == null) {
                payedSum = BigDecimal.ZERO;
            }
            if (sum.compareTo(subscriberIdKeyBalanceValue.getValue().add(payedSum.negate())) <= 0) {
                List<SubscriberSession> subscriberSessions = subscriberDAO.getSubscriberSessions(subscriberIdKeyBalanceValue.getKey());
                if (subscriberSessions.size() != 0) {
                    List<SubscriberSession> sortedSubscriberSessions = subscriberSessions.stream().sorted((e1, e2) -> e1.getConnectionDate().compareTo(e2.getConnectionDate())).collect(Collectors.toList());
                    SubscriberSession lastSession = sortedSubscriberSessions.get(sortedSubscriberSessions.size() - 1);
                    if (lastSession.getDisconnectionDate() != null && lastSession.getDisconnectionDate().compareTo(date) >= 0) {

                        SubscriberDebit subscriberDebit = new SubscriberDebit();
                        subscriberDebit.setSubscriberAccount(subscriberIdKeyBalanceValue.getKey());
                        subscriberDebit.setDebit(subscriberIdKeyBalanceValue.getValue().add(payedSum.negate()).setScale(2, BigDecimal.ROUND_HALF_UP));
                        subscriberDebit.setSubscriber(subscriberDAO.get(subscriberDebit.getSubscriberAccount()));
                        if (subscriberDebit.getSubscriber() != null) {
                            subscriberDebit.setSubscriberStreet(streetDAO.get(subscriberDebit.getSubscriber().getStreetId()));

                            subscriberDebitsResult.add(subscriberDebit);
                        }
                    }
                }
            }
        }
        System.out.println(subscriberDebitsResult.size());
        return subscriberDebitsResult;
    }

    public String generateExcelReport(LocalDate date, BigDecimal sum) {
        SubscriberDebitExcel subscriberDebitExcel = new SubscriberDebitExcel("debtorReport");

        subscriberDebitExcel.setSubscriberDebitList(getData(date, sum));

        return subscriberDebitExcel.generate();
    }

}
