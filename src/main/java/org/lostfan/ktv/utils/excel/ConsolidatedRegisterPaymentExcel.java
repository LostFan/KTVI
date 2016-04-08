package org.lostfan.ktv.utils.excel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.FixedServices;
import org.lostfan.ktv.utils.ResourceBundles;

public class ConsolidatedRegisterPaymentExcel implements ExcelGenerator{

    private List<Payment> payments;

    private LocalDate date;

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
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
            Integer NUMBER_OF_DAY_COLUMN = i++;
            Integer SUBSCRIPTION_FEE_COLUMN = i++;
            Integer CONNECTION_COLUMN = i++;
            Integer ADDITIONAL_COLUMN = i++;
            Integer TOTAL_COLUMN = i++;

            //Creating WorkBook
            String fileName = String.format("%s - %d.%d", ResourceBundles.getEntityBundle().getString(
                    "dailyRegister"), date.getMonth().getValue(), date.getYear());
            File file = new File(fileName + ".xls");
            workbook = Workbook.createWorkbook(file);
            //Creating sheet
            WritableSheet sheet = workbook.createSheet("PAGE 1", 0);
            sheet.setColumnView(NUMBER_OF_DAY_COLUMN, 8);
            sheet.setColumnView(SUBSCRIPTION_FEE_COLUMN, 20);
            sheet.setColumnView(CONNECTION_COLUMN, 20);
            sheet.setColumnView(ADDITIONAL_COLUMN, 20);
            sheet.setColumnView(TOTAL_COLUMN, 20);
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setAlignment(Alignment.CENTRE);
            i = 0;

            // Adding cells
            sheet.addCell(new Label(NUMBER_OF_DAY_COLUMN, i, getGuiString("day"), cellFormat));
            sheet.addCell(new Label(SUBSCRIPTION_FEE_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriptionFee"), cellFormat));
            sheet.addCell(new Label(CONNECTION_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "connection"), cellFormat));
            sheet.addCell(new Label(ADDITIONAL_COLUMN, i, ("additionalServices"), cellFormat));
            sheet.addCell(new Label(TOTAL_COLUMN, i, getGuiString("total"), cellFormat));
            i++;

            for (Integer index = 1; index <= date.lengthOfMonth(); index++) {
                final Integer finalIndex = index;
                sheet.addCell(new Number(NUMBER_OF_DAY_COLUMN, i, finalIndex));
                sheet.addCell(new Number(SUBSCRIPTION_FEE_COLUMN, i, payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.SUBSCRIPTION_FEE.getId())
                        .filter(e -> e.getDate().getDayOfMonth() == finalIndex + 1)
                        .mapToInt(e -> e.getPrice()).sum()));
                sheet.addCell(new Number(CONNECTION_COLUMN, i, payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.CONNECTION.getId())
                        .filter(e -> e.getDate().getDayOfMonth() == finalIndex + 1)
                        .mapToInt(e -> e.getPrice()).sum()));
                sheet.addCell(new Number(ADDITIONAL_COLUMN, i, payments.stream().filter(e -> e.getServicePaymentId() != FixedServices.SUBSCRIPTION_FEE.getId())
                        .filter(e -> e.getServicePaymentId() != FixedServices.CONNECTION.getId())
                        .filter(e -> e.getDate().getDayOfMonth() == finalIndex + 1)
                        .mapToInt(e -> e.getPrice()).sum()));
                sheet.addCell(new Number(TOTAL_COLUMN, i, payments.stream()
                        .filter(e -> e.getDate().getDayOfMonth() == finalIndex + 1)
                        .mapToInt(e -> e.getPrice()).sum()));
                i++;
            }

            sheet.addCell(new Label(NUMBER_OF_DAY_COLUMN, i, getGuiString("inTotal")));
            sheet.addCell(new Number(SUBSCRIPTION_FEE_COLUMN, i, payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.SUBSCRIPTION_FEE.getId())
                    .mapToInt(e -> e.getPrice()).sum()));
            sheet.addCell(new Number(CONNECTION_COLUMN, i, payments.stream().filter(e -> e.getServicePaymentId() == FixedServices.CONNECTION.getId())
                    .mapToInt(e -> e.getPrice()).sum()));
            sheet.addCell(new Number(ADDITIONAL_COLUMN, i, payments.stream().filter(e -> e.getServicePaymentId() != FixedServices.SUBSCRIPTION_FEE.getId())
                    .filter(e -> e.getServicePaymentId() != FixedServices.CONNECTION.getId())
                    .mapToInt(e -> e.getPrice()).sum()));
            sheet.addCell(new Number(TOTAL_COLUMN, i, payments.stream()
                    .mapToInt(e -> e.getPrice()).sum()));
            i++;

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

    private static String getGuiString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
