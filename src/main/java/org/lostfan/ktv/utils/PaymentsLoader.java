package org.lostfan.ktv.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.entity.PaymentEntityModel;

public class PaymentsLoader {

    private File file;

    public PaymentsLoader(File file) {
        this.file = file;
    }

    public List<Payment> loadPayments() {
        List<Payment> payments = new ArrayList<>();
        if(file.getName().endsWith(".210")) {
            payments = loadERIP();
        }
        if(file.getName().endsWith(".dat")) {
            payments = loadPost();
        }

        return payments;
    }

    private List<Payment> loadERIP() {
        List<Payment> payments = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String str[] = sCurrentLine.split("\\^");
                try {
                    Payment payment = new Payment();
                    payment.setSubscriberAccount(Integer.parseInt(str[2]));
                    payment.setDate(createDate(str[9]));
                    payment.setBankFileName(file.getName());
                    payment.setPrice(Integer.parseInt(str[6].split("\\.")[0]));
                    payments.add(payment);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return payments;
    }

    private List<Payment> loadPost() {
        List<Payment> payments = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String str[] = sCurrentLine.split(";");
                try {
                    Payment payment = new Payment();
                    payment.setSubscriberAccount(Integer.parseInt(str[0]));
//                    payment.setDate(createDate(str[9]));
                    payment.setBankFileName(file.getName());
                    payment.setPrice(Integer.parseInt(str[5]));
                    payments.add(payment);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return payments;
    }

    private LocalDate createDate(String s) {
        return LocalDate.of(Integer.parseInt(s.substring(0, 4)), Integer.parseInt(s.substring(4, 6)), Integer.parseInt(s.substring(6, 8)));
    }

}
