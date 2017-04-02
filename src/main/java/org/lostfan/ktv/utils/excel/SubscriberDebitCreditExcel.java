package org.lostfan.ktv.utils.excel;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.*;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.dto.PaymentExt;
import org.lostfan.ktv.model.dto.RenderedServiceAndPayment;
import org.lostfan.ktv.model.dto.SubscriberDebit;
import org.lostfan.ktv.utils.DateFormatter;
import org.lostfan.ktv.utils.ResourceBundles;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SubscriberDebitCreditExcel implements ExcelGenerator {

    private List<PaymentExt> payments;
    private List<RenderedServiceAndPayment> renderedServiceAndPayments;

    public void setPayments(List<PaymentExt> payments) {
        this.payments = payments;
    }

    public void setRenderedServicesAndPayments(List<RenderedServiceAndPayment> renderedServiceAndPayments) {
        this.renderedServiceAndPayments = renderedServiceAndPayments;
    }

    private List<SubscriberDebit> subscriberDebitList;

    public void setSubscriberDebitList(List<SubscriberDebit> subscriberDebitList) {
        this.subscriberDebitList = subscriberDebitList;
    }

    private Integer subscriberAccount;

    public SubscriberDebitCreditExcel(Integer subscriberAccount) {
        this.subscriberAccount = subscriberAccount;
    }

    @Override
    public String generate() {

        WritableWorkbook workbook;
        String message = null;
        try {
            Integer row = 0;
            Integer RS_DATE = row++;
            Integer RS_FORWARD_BALANCE = row++;
            Integer RS_CHARGEABLE = row++;
            row+=2;
            Integer P_DATE = row++;
            Integer P_PAID = row++;
            //Creating WorkBook
            String fileName = String.format("%d - %s",
                    this.subscriberAccount, DateFormatter.format(LocalDate.now()));
            File file = new File(fileName + ".xls");
            workbook = Workbook.createWorkbook(file);
            //Creating sheet
            WritableSheet sheet = workbook.createSheet("PAGE 1", 0);
            sheet.setColumnView(RS_DATE, 8);
            sheet.setColumnView(RS_FORWARD_BALANCE, 20);
            sheet.setColumnView(RS_CHARGEABLE, 30);
            sheet.setColumnView(P_DATE, 20);
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setAlignment(Alignment.CENTRE);
            row = 0;

            //Addding cells
            sheet.addCell(new Label(RS_DATE, row, ResourceBundles.getEntityBundle().getString(
                    "renderedService.date"), cellFormat));
            sheet.addCell(new Label(RS_FORWARD_BALANCE, row, ResourceBundles.getGuiBundle().getString(
                    "broughtForwardBalance"), cellFormat));
            sheet.addCell(new Label(RS_CHARGEABLE, row, ResourceBundles.getGuiBundle().getString(
                    "chargeable"), cellFormat));

            sheet.addCell(new Label(P_DATE, row, ResourceBundles.getEntityBundle().getString(
                    "renderedService.date"), cellFormat));
            sheet.addCell(new Label(P_PAID, row, ResourceBundles.getGuiBundle().getString(
                    "paid"), cellFormat));
            row++;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM.yyyy");
            for (PaymentExt payment : payments) {
                sheet.addCell(new Label(P_DATE, row, dateTimeFormatter.format(payment.getDate())));
                sheet.addCell(new Number(P_PAID, row, payment.getPrice().doubleValue()));
                row++;
            }
            row = 1;
            LocalDate date = LocalDate.now().withDayOfMonth(1);
            if (this.renderedServiceAndPayments.size() > 0) {
                date = this.renderedServiceAndPayments.stream()
                        .min((o1, o2) -> o1.getDate().compareTo(o2.getDate())).get().getDate();
            }
            while (date.isBefore(LocalDate.now())) {
                final LocalDate fDate = date.withDayOfMonth(1);
                sheet.addCell(new Label(RS_DATE, row, dateTimeFormatter.format(fDate)));
                sheet.addCell(new Number(RS_FORWARD_BALANCE, row, this.renderedServiceAndPayments.stream()
                        .filter(o -> o.getDate().isBefore(fDate))
                        .map(o -> o.isCredit() ? o.getPrice().negate() : o.getPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                sheet.addCell(new Number(RS_CHARGEABLE, row, renderedServiceAndPayments.stream()
                        .filter(o -> o.getDate().withDayOfMonth(1).isEqual(fDate))
                        .filter(o -> !o.isCredit())
                        .map(o -> o.getPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                date = date.plusMonths(1);
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
