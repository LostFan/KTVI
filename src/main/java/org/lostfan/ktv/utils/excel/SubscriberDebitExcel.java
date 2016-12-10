package org.lostfan.ktv.utils.excel;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.*;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.SubscriberDebit;
import org.lostfan.ktv.utils.DateFormatter;
import org.lostfan.ktv.utils.ResourceBundles;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class SubscriberDebitExcel implements ExcelGenerator {

    private List<SubscriberDebit> subscriberDebitList;

    public void setSubscriberDebitList(List<SubscriberDebit> subscriberDebitList) {
        this.subscriberDebitList = subscriberDebitList;
    }

    private String fileName;

    public SubscriberDebitExcel(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String generate() {

        WritableWorkbook workbook;
        String message = null;
        try {
            Integer row = 0;
            Integer SUBSCRIBER_ID_COLUMN = row++;
            Integer SUBSCRIBER_NAME_COLUMN = row++;
            Integer ADDRESS_COLUMN = row++;
            Integer PAYMENT_PRICE = row++;
            //Creating WorkBook
            String fileName = String.format("%s - %s", ResourceBundles.getEntityBundle().getString(
                    this.fileName), DateFormatter.format(LocalDate.now()));
            File file = new File(fileName + ".xls");
            workbook = Workbook.createWorkbook(file);
            //Creating sheet
            WritableSheet sheet = workbook.createSheet("PAGE 1", 0);
            sheet.setColumnView(SUBSCRIBER_ID_COLUMN, 8);
            sheet.setColumnView(SUBSCRIBER_NAME_COLUMN, 20);
            sheet.setColumnView(ADDRESS_COLUMN, 30);
            sheet.setColumnView(PAYMENT_PRICE, 20);
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setAlignment(Alignment.CENTRE);
            row = 0;

            //Addding cells
            sheet.addCell(new Label(SUBSCRIBER_ID_COLUMN, row, ResourceBundles.getEntityBundle().getString(
                    "subscriber"), cellFormat));
            sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, row, ResourceBundles.getEntityBundle().getString(
                    "subscriber.name"), cellFormat));
            sheet.addCell(new Label(ADDRESS_COLUMN, row, ResourceBundles.getGuiBundle().getString(
                    "address"), cellFormat));

            sheet.addCell(new Label(PAYMENT_PRICE, row, ResourceBundles.getEntityBundle().getString(
                    "payment.price"), cellFormat));
            row++;

            for (SubscriberDebit subscriberDebit : subscriberDebitList) {
                sheet.addCell(new Number(SUBSCRIBER_ID_COLUMN, row, subscriberDebit.getSubscriberAccount()));
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, row, getAbbreviatedName(subscriberDebit.getSubscriber())));
                sheet.addCell(new Label(ADDRESS_COLUMN, row, getFullSubscriberAddress(subscriberDebit)));
                sheet.addCell(new Number(PAYMENT_PRICE, row, subscriberDebit.getDebit().doubleValue()));
                row++;
            }


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

    private String getAbbreviatedName(Subscriber subscriber) {
        String abbreviatedName = subscriber.getName();
        String[] strings = abbreviatedName.split("\\s+");
        if (strings.length < 2) {
            return subscriber.getName();
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

    private String getFullSubscriberAddress(SubscriberDebit dto) {
        Subscriber subscriber = dto.getSubscriber();
        if (subscriber == null || dto.getSubscriberStreet() == null) {
            return "";
        }
        StringBuilder address = new StringBuilder(dto.getSubscriberStreet().getName())
                .append(",")
                .append(subscriber.getHouse())
                .append(subscriber.getIndex());
        if (!subscriber.getBuilding().isEmpty()) {
            address.append(",")
                    .append(getGuiString("buildingAbbreviated"))
                    .append(subscriber.getBuilding());
        }
        address.append(",")
                .append(getGuiString("flatAbbreviated"))
                .append(subscriber.getFlat());
        return address.toString();
    }

    private static String getGuiString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
