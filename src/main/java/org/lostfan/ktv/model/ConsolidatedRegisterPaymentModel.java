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
import org.lostfan.ktv.model.entity.BaseModel;
import org.lostfan.ktv.model.transform.PaymentTransformer;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.utils.excel.ConsolidatedRegisterPaymentExcel;

public class ConsolidatedRegisterPaymentModel extends BaseObservable implements BaseModel {

    private ServiceDAO serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    private SubscriberDAO subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    private PaymentDAO paymentDAO = DAOFactory.getDefaultDAOFactory().getPaymentDAO();

    private PaymentTransformer paymentTransformer = new PaymentTransformer();

    public ConsolidatedRegisterPaymentModel() {
    }

    @Override
    public String getEntityNameKey() {
        return "dailyRegister";
    }

    @Override
    public List<EntityField> getFields() {
        return new ArrayList<>();
    }

    public List<Payment> getByMonth(LocalDate date) {
        return paymentDAO.getByMonth(date);
    }

    public String generateExcelReport(LocalDate date) {
        ConsolidatedRegisterPaymentExcel consolidatedRegisterPaymentExcel = new ConsolidatedRegisterPaymentExcel();
        consolidatedRegisterPaymentExcel.setPayments(getByMonth(date));
        consolidatedRegisterPaymentExcel.setDate(date);
        return consolidatedRegisterPaymentExcel.generate();
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAll();
    }


}
