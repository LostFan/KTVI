package org.lostfan.ktv.utils.excel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.utils.DateFormatter;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.SubscriberByAddressComparator;

public class DailyRegisterExcel implements ExcelGenerator {

    private List<PaymentExt> paymentExts;

    private List<Service> services;

    private LocalDate date;

    public void setPaymentExts(List<PaymentExt> paymentExts) {
        this.paymentExts = paymentExts;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String generate() {

        WritableWorkbook workbook;
        String message = null;
        try {
            Integer i = 0;
            Integer SUBSCRIBER_ID_COLUMN = i++;
            Integer SUBSCRIBER_NAME_COLUMN = i++;
            Integer SERVICE_COLUMN = i++;
            Integer PAYMENT_PRICE = i++;
            //Creating WorkBook
            String fileName = String.format("%s - %s", ResourceBundles.getEntityBundle().getString(
                    "dailyRegister"), DateFormatter.format(date));
            File file = new File(fileName + ".xls");
            workbook = Workbook.createWorkbook(file);
            //Creating sheet
            WritableSheet sheet = workbook.createSheet("PAGE 1", 0);
            sheet.setColumnView(SUBSCRIBER_ID_COLUMN, 8);
            sheet.setColumnView(SUBSCRIBER_NAME_COLUMN, 20);
            sheet.setColumnView(PAYMENT_PRICE, 20);
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setAlignment(Alignment.CENTRE);
            i = 0;

            //Addding cells
            sheet.addCell(new Label(SUBSCRIBER_ID_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriber"), cellFormat));
            sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriber.name"), cellFormat));
            sheet.addCell(new Label(SERVICE_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "service"), cellFormat));

            sheet.addCell(new Label(PAYMENT_PRICE, i, ResourceBundles.getEntityBundle().getString(
                    "payment.price"), cellFormat));
            i++;

            SubscriberByAddressComparator comparator = new SubscriberByAddressComparator();
            paymentExts = paymentExts.stream()
                    .sorted((o1, o2) -> comparator.compare(o1.getSubscriber(), o2.getSubscriber()))
                    .collect(Collectors.toList());

            for (PaymentExt paymentExt : paymentExts) {
                sheet.addCell(new Number(SUBSCRIBER_ID_COLUMN, i, paymentExt.getSubscriberAccount()));
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, getAbbreviatedName(paymentExt)));
                sheet.addCell(new Number(SERVICE_COLUMN, i, paymentExt.getServicePaymentId()));
                sheet.addCell(new Number(PAYMENT_PRICE, i, paymentExt.getPrice()));
                i++;
            }

            Long allPayment = 0L;

            for (Service service : services) {
                Long servicePayment = paymentExts.stream()
                        .filter(e -> e.getServicePaymentId().equals(service.getId()))
                        .mapToLong(value -> value.getPrice())
                        .sum();

                allPayment += servicePayment;
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, service.getName()));
                sheet.addCell(new Number(SERVICE_COLUMN, i, service.getId()));
                sheet.addCell(new Number(PAYMENT_PRICE, i, servicePayment));
                i++;
            }
            sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, getGuiString("inTotal")));
            sheet.addCell(new Number(PAYMENT_PRICE, i, allPayment));

            workbook.write();
            workbook.close();
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            try {
                desktop.open(file);
            } catch (IOException | NullPointerException ioe) {
                message = "message.fail";
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {

                message = "message.fileIsUsed";
            } else {
                message = "message.fail";
            }
        } catch (WriteException e) {
            message = "message.fail";
        }
        return message;
    }

    private String getAbbreviatedName(PaymentExt paymentExt) {
        String abbreviatedName = paymentExt.getSubscriber().getName();
        String[] strings = abbreviatedName.split("\\s+");
        if (strings.length < 2) {
            return paymentExt.getSubscriber().getName();
        }
        StringBuilder name = new StringBuilder()
                .append(strings[0])
                .append(" ")
                .append(strings[1].charAt(0))
                .append(".");
        if (strings.length > 2) {
            name.append(strings[2].charAt(0))
                    .append(". ");
        }
        return name.toString();
    }

    private static String getGuiString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
