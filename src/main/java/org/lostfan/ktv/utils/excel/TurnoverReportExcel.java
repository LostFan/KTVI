package org.lostfan.ktv.utils.excel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.dto.TurnoverSheetTableDTO;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.SubscriberByAddressComparator;

public class TurnoverReportExcel implements ExcelGenerator {

    private List<TurnoverSheetTableDTO> turnoverSheetTableDTOs;

    private List<Service> additionalServices;

    private Service service;

    private LocalDate date;

    private Boolean isAdditional;

    public void setTurnoverSheetTableDTOs(List<TurnoverSheetTableDTO> turnoverSheetTableDTOs) {
        this.turnoverSheetTableDTOs = turnoverSheetTableDTOs;
    }

    public void setAdditionalServices(List<Service> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public void setService(Service service) {
        this.service = service;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setIsAdditional(Boolean isAdditional) {
        this.isAdditional = isAdditional;
    }

    @Override
    public String generate() {
        WritableWorkbook workbook;
        String message = null;
        try {
            int i = 0;
            Integer FIRST_COLUMN = i;

            Integer SUBSCRIBER_ID_COLUMN = i++;
            Integer SUBSCRIBER_ADDRESS_COLUMN = i++;
            Integer SUBSCRIBER_NAME_COLUMN = i++;
            Integer SERVICE_COLUMN = isAdditional ? i++ : 0;
            Integer BROUGHT_FORWARD_BALANCE_DEBIT = i++;
            Integer BROUGHT_FORWARD_BALANCE_CREDIT = i++;
            Integer TURNOVER_BALANCE_DEBIT = i++;
            Integer TURNOVER_BALANCE_CREDIT = i++;
            Integer CARRIED_FORWARD_BALANCE_DEBIT = i++;
            Integer CARRIED_FORWARD_BALANCE_CREDIT = i++;
            Integer LAST_COLUMN = CARRIED_FORWARD_BALANCE_CREDIT;

            //Creating WorkBook
            String fileName;
            if (isAdditional) {
                fileName = String.format("%s %d-%d.xls", getGuiString("additionalServices"),
                        date.getMonthValue(), date.getYear());
            } else {
                fileName = String.format("%s %d-%d.xls", service.toString(), date.getMonthValue(), date.getYear());
            }
            File file = new File(fileName);
            workbook = Workbook.createWorkbook(file);
            //Creating sheet
            WritableSheet sheet = workbook.createSheet("PAGE 1", 0);
            sheet.setColumnView(SUBSCRIBER_ID_COLUMN, 6);
            sheet.setColumnView(SUBSCRIBER_ADDRESS_COLUMN, 25);
            sheet.setColumnView(SUBSCRIBER_NAME_COLUMN, 20);
            sheet.setColumnView(TURNOVER_BALANCE_DEBIT, 10);
            sheet.setColumnView(TURNOVER_BALANCE_CREDIT, 10);
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setAlignment(Alignment.CENTRE);
            i = 0;
            sheet.mergeCells(FIRST_COLUMN, i, LAST_COLUMN, i);
            i++;
            sheet.mergeCells(BROUGHT_FORWARD_BALANCE_DEBIT, i, BROUGHT_FORWARD_BALANCE_CREDIT, i);
            sheet.mergeCells(TURNOVER_BALANCE_DEBIT, i, TURNOVER_BALANCE_CREDIT, i);
            sheet.mergeCells(CARRIED_FORWARD_BALANCE_DEBIT, i, CARRIED_FORWARD_BALANCE_CREDIT, i);

            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_DEBIT, i, getGuiString(
                    "broughtForwardBalance"), cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_DEBIT, i, getGuiString(
                    "turnoverBalance"), cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_DEBIT, i, getGuiString(
                    "carriedForwardBalance"), cellFormat));
            i++;
            //Addding cells
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_ID_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriber"), cellFormat));
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_ADDRESS_COLUMN, i, getGuiString(
                    "address"), cellFormat));
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_NAME_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriber.name"), cellFormat));
            if (isAdditional) {
                sheet.addCell(new jxl.write.Label(SERVICE_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                        "service"), cellFormat));
            }
            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_CREDIT, i, getGuiString(
                    "credit"), cellFormat));
            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_DEBIT, i, getGuiString(
                    "debit"), cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_CREDIT, i,getGuiString(
                    "credit"), cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_DEBIT, i, getGuiString(
                    "debit"), cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_CREDIT, i, getGuiString(
                    "credit"), cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_DEBIT, i, getGuiString(
                    "debit"), cellFormat));
            i++;

            SubscriberByAddressComparator comparator = new SubscriberByAddressComparator();
            turnoverSheetTableDTOs = turnoverSheetTableDTOs.stream()
                    .sorted((o1, o2) -> comparator.compare(o1.getSubscriber(), o2.getSubscriber()))
                    .collect(Collectors.toList());

            for (TurnoverSheetTableDTO turnoverSheetTableDTO : turnoverSheetTableDTOs) {
                sheet.addCell(new Number(SUBSCRIBER_ID_COLUMN, i, turnoverSheetTableDTO.getSubscriberAccount()));
                sheet.addCell(new Label(SUBSCRIBER_ADDRESS_COLUMN, i, getFullSubscriberAddress(turnoverSheetTableDTO)));
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, getAbbreviatedName(turnoverSheetTableDTO)));
                if (isAdditional) {
                    sheet.addCell(new Number(SERVICE_COLUMN, i, turnoverSheetTableDTO.getServiceId()));
                }
                sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_CREDIT, i, turnoverSheetTableDTO.getBroughtForwardBalanceCredit().doubleValue()));
                sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_DEBIT, i, turnoverSheetTableDTO.getBroughtForwardBalanceDebit().doubleValue()));
                sheet.addCell(new Number(TURNOVER_BALANCE_CREDIT, i, turnoverSheetTableDTO.getTurnoverBalanceCredit().doubleValue()));
                sheet.addCell(new Number(TURNOVER_BALANCE_DEBIT, i, turnoverSheetTableDTO.getTurnoverBalanceDebit().doubleValue()));
                sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_CREDIT, i, turnoverSheetTableDTO.getCarriedForwardBalanceCredit().doubleValue()));
                sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_DEBIT, i, turnoverSheetTableDTO.getCarriedForwardBalanceDebit().doubleValue()));
                i++;
            }

            if (isAdditional) {
                for (Service additionalService : additionalServices) {
                    Double broughtForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId().equals(additionalService.getId())).map(value -> value.getBroughtForwardBalanceCredit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
                    Double broughtForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId().equals(additionalService.getId())).map(value -> value.getBroughtForwardBalanceDebit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
                    Double carriedForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId().equals(additionalService.getId())).map(value -> value.getCarriedForwardBalanceCredit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
                    Double carriedForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId().equals(additionalService.getId())).map(value -> value.getCarriedForwardBalanceDebit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
                    Double turnoverBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId().equals(additionalService.getId())).map(value -> value.getTurnoverBalanceCredit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
                    Double turnoverBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId().equals(additionalService.getId())).map(value -> value.getTurnoverBalanceDebit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();

                    sheet.addCell(new Label(SUBSCRIBER_ADDRESS_COLUMN, i, additionalService.getName()));
                    sheet.addCell(new Number(SERVICE_COLUMN, i, additionalService.getId()));
                    sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_CREDIT, i, broughtForwardBalanceCreditSum));
                    sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_DEBIT, i, broughtForwardBalanceDebitSum));
                    sheet.addCell(new Number(TURNOVER_BALANCE_CREDIT, i, turnoverBalanceCreditSum));
                    sheet.addCell(new Number(TURNOVER_BALANCE_DEBIT, i, turnoverBalanceDebitSum));
                    sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_CREDIT, i, carriedForwardBalanceCreditSum));
                    sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_DEBIT, i, carriedForwardBalanceDebitSum));
                    i++;
                }
            }
            Double broughtForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().map(e -> e.getBroughtForwardBalanceCredit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
            Double broughtForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().map(e -> e.getBroughtForwardBalanceDebit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
            Double carriedForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().map(e -> e.getCarriedForwardBalanceCredit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
            Double carriedForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().map(e -> e.getCarriedForwardBalanceDebit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
            Double turnoverBalanceCreditSum = turnoverSheetTableDTOs.stream().map(e -> e.getTurnoverBalanceCredit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
            Double turnoverBalanceDebitSum = turnoverSheetTableDTOs.stream().map(e -> e.getTurnoverBalanceDebit()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();

            sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_CREDIT, i, broughtForwardBalanceCreditSum));
            sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_DEBIT, i, broughtForwardBalanceDebitSum));
            sheet.addCell(new Number(TURNOVER_BALANCE_CREDIT, i, turnoverBalanceCreditSum));
            sheet.addCell(new Number(TURNOVER_BALANCE_DEBIT, i, turnoverBalanceDebitSum));
            sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_CREDIT, i, carriedForwardBalanceCreditSum));
            sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_DEBIT, i, carriedForwardBalanceDebitSum));

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

    private String getFullSubscriberAddress(TurnoverSheetTableDTO dto) {
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

    private String getAbbreviatedName(TurnoverSheetTableDTO dto) {
        String abbreviatedName = dto.getSubscriber().getName();
        String[] strings = abbreviatedName.split("\\s+");
        if (strings.length < 2) {
            return dto.getSubscriber().getName();
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
