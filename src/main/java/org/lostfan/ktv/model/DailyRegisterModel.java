package org.lostfan.ktv.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.PaymentDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.domain.Service;
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

    public DailyRegisterModel() {
    }

    @Override
    public String getEntityNameKey() {
        return "dailyRegister";
    }

    @Override
    public List<EntityField> getFields() {
        return new ArrayList<>();
    }

    public List<PaymentExt> getPaymentsExtByDate(LocalDate date) {
        List<PaymentExt> payments = new ArrayList<>();
        for (Payment payment : paymentDAO.getByDate(date)) {
            PaymentExt paymentExt = paymentTransformer.transformTo(payment);
            paymentExt.setService(serviceDAO.get(payment.getServicePaymentId()));
            paymentExt.setSubscriber(subscriberDAO.get(payment.getSubscriberAccount()));
            payments.add(paymentExt);
        }
        return payments;
    }

    public String generateExcelReport(LocalDate date) {
        DailyRegisterExcel dailyRegisterExcel = new DailyRegisterExcel();
        dailyRegisterExcel.setPaymentExts(getPaymentsExtByDate(date));
        dailyRegisterExcel.setServices(getAllServices());
        dailyRegisterExcel.setDate(date);
        return dailyRegisterExcel.generate();
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAll();
    }


}
