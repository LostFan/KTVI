package org.lostfan.ktv.utils;

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
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.DailyRegisterModel;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.TurnoverSheetTableDTO;

public class DailyRegisterExcel {
    DailyRegisterModel model;


    public DailyRegisterExcel(DailyRegisterModel model) {
        this.model = model;
    }

    public String generate(LocalDate date) {

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
//            sheet.mergeCells(FIRST_COLUMN, i, LAST_COLUMN, i);
//            i++;

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

            List<PaymentExt> paymentExts;
            paymentExts = model.getPaymentsExtByDate(date);


            paymentExts = paymentExts.stream().sorted((o1, o2) -> {
                if (o1.getSubscriber().getStreetId() - o2.getSubscriber().getStreetId() != 0) {
                    return o1.getSubscriber().getStreetId() - o2.getSubscriber().getStreetId();
                }
                if (o1.getSubscriber().getHouse() - o2.getSubscriber().getHouse() != 0) {
                    return o1.getSubscriber().getHouse() - o2.getSubscriber().getHouse();
                }
                if (o1.getSubscriber().getIndex().compareToIgnoreCase(o2.getSubscriber().getIndex()) != 0) {
                    return o1.getSubscriber().getIndex().compareToIgnoreCase(o2.getSubscriber().getIndex());
                }
                if (o1.getSubscriber().getBuilding().compareToIgnoreCase(o2.getSubscriber().getBuilding()) != 0) {
                    return o1.getSubscriber().getBuilding().compareToIgnoreCase(o2.getSubscriber().getBuilding());
                }
                if (o1.getSubscriber().getFlat().length() - o2.getSubscriber().getFlat().length() != 0) {
                    return o1.getSubscriber().getFlat().length() - o2.getSubscriber().getFlat().length();
                }
                return o1.getSubscriber().getFlat().compareToIgnoreCase(o2.getSubscriber().getFlat());
            }).collect(Collectors.toList());


            for (PaymentExt paymentExt : paymentExts) {
                sheet.addCell(new Number(SUBSCRIBER_ID_COLUMN, i, paymentExt.getSubscriberAccount()));
//                sheet.addCell(new Label(SUBSCRIBER_ADDRESS_COLUMN, i, getFullSubscriberAddress(paymentExt)));
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, getAbbreviatedName(paymentExt)));
                sheet.addCell(new Number(SERVICE_COLUMN, i, paymentExt.getServicePaymentId()));
                sheet.addCell(new Number(PAYMENT_PRICE, i, paymentExt.getPrice()));
                i++;
            }

            Long allPayment = 0L;

            List<Service> services = model.getAllServices();
            for (Service service : services) {
                Long servicePayment = paymentExts.stream().filter(e -> e.getServicePaymentId() == service.getId()).mapToLong(value -> value.getPrice()).sum();

                allPayment += servicePayment;
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, service.getName()));
                sheet.addCell(new Number(SERVICE_COLUMN, i, service.getId()));
                sheet.addCell(new Number(PAYMENT_PRICE, i, servicePayment));
                i++;
            }
            sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, getGuiString("total")));
            sheet.addCell(new Number(PAYMENT_PRICE, i, allPayment));

            workbook.write();
            workbook.close();
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            try {
                desktop.open(file);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {

//                exceptionWindow(e);
                message = "message.fileIsUsed";
            }
            // TODO Auto-generated catch block
//            e.printStackTrace();
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }

//    private String getFullSubscriberAddress(PaymentExt paymentExt) {
//        Subscriber subscriber = paymentExt.getSubscriber();
//        if (subscriber == null || paymentExt.getSubscriberStreet() == null) {
//            return "";
//        }
//        StringBuilder address = new StringBuilder(paymentExt.getSubscriberStreet().getName())
//                .append(",")
//                .append(subscriber.getHouse())
//                .append(subscriber.getIndex());
//        if (!subscriber.getBuilding().isEmpty()) {
//            address.append(",")
//                    .append(getGuiString("buildingAbbreviated"))
//                    .append(subscriber.getBuilding());
//        }
//        address.append(",")
//                .append(getGuiString("flatAbbreviated"))
//                .append(subscriber.getFlat());
//        return address.toString();
//    }

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
