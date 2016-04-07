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
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.dto.TurnoverSheetTableDTO;
import org.lostfan.ktv.utils.ResourceBundles;

public class TurnoverReportExcel {

    private List<TurnoverSheetTableDTO> turnoverSheetTableDTOs;

    private List<Service> additionalServices;

    private Service service;

    private LocalDate date;

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

    public String generate(Boolean isAdditional) {

        WritableWorkbook workbook;
        String message = null;
        try {
            Integer FIRST_COLUMN;
            Integer LAST_COLUMN;
            Integer SUBSCRIBER_ID_COLUMN;
            Integer SUBSCRIBER_ADDRESS_COLUMN;
            Integer SUBSCRIBER_NAME_COLUMN;
            Integer SERVICE_COLUMN;
            Integer BROUGHT_FORWARD_BALANCE_DEBIT;
            Integer BROUGHT_FORWARD_BALANCE_CREDIT;
            Integer TURNOVER_BALANCE_DEBIT;
            Integer TURNOVER_BALANCE_CREDIT;
            Integer CARRIED_FORWARD_BALANCE_DEBIT;
            Integer CARRIED_FORWARD_BALANCE_CREDIT;

            if (isAdditional) {
                Integer i = 0;
                FIRST_COLUMN = i;

                SUBSCRIBER_ID_COLUMN = i++;
                SUBSCRIBER_ADDRESS_COLUMN = i++;
                SUBSCRIBER_NAME_COLUMN = i++;
                SERVICE_COLUMN = i++;
                BROUGHT_FORWARD_BALANCE_DEBIT = i++;
                BROUGHT_FORWARD_BALANCE_CREDIT = i++;
                TURNOVER_BALANCE_DEBIT = i++;
                TURNOVER_BALANCE_CREDIT = i++;
                CARRIED_FORWARD_BALANCE_DEBIT = i++;
                CARRIED_FORWARD_BALANCE_CREDIT = i++;
                LAST_COLUMN = CARRIED_FORWARD_BALANCE_CREDIT;
            } else {
                Integer i = 0;
                FIRST_COLUMN = i;

                SUBSCRIBER_ID_COLUMN = i++;
                SUBSCRIBER_ADDRESS_COLUMN = i++;
                SUBSCRIBER_NAME_COLUMN = i++;
                SERVICE_COLUMN = 0;
                BROUGHT_FORWARD_BALANCE_DEBIT = i++;
                BROUGHT_FORWARD_BALANCE_CREDIT = i++;
                TURNOVER_BALANCE_DEBIT = i++;
                TURNOVER_BALANCE_CREDIT = i++;
                CARRIED_FORWARD_BALANCE_DEBIT = i++;
                CARRIED_FORWARD_BALANCE_CREDIT = i++;
                LAST_COLUMN = CARRIED_FORWARD_BALANCE_CREDIT;
            }
            //Creating WorkBook
            String fileName;
            if (isAdditional) {
                fileName = ResourceBundles.getGuiBundle().getString(
                        "additionalServices") + " "
                        + date.getMonthValue() + "-" + date.getYear();
            } else {
                fileName = service.toString() + " "
                        + date.getMonthValue() + "-" + date.getYear();
            }
            File file = new File(fileName + ".xls");
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
            int i = 0;
            sheet.mergeCells(FIRST_COLUMN, i, LAST_COLUMN, i);
            i++;
            sheet.mergeCells(BROUGHT_FORWARD_BALANCE_DEBIT, i, BROUGHT_FORWARD_BALANCE_CREDIT, i);
            sheet.mergeCells(TURNOVER_BALANCE_DEBIT, i, TURNOVER_BALANCE_CREDIT, i);
            sheet.mergeCells(CARRIED_FORWARD_BALANCE_DEBIT, i, CARRIED_FORWARD_BALANCE_CREDIT, i);

            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "broughtForwardBalance"), cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "turnoverBalance"), cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "carriedForwardBalance"), cellFormat));
            i++;
            //Addding cells
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_ID_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriber"), cellFormat));
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_ADDRESS_COLUMN, i, ResourceBundles.getGuiBundle().getString(
                    "address"), cellFormat));
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_NAME_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriber.name"), cellFormat));
            if (isAdditional) {
                sheet.addCell(new jxl.write.Label(SERVICE_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                        "service"), cellFormat));
            }
            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_CREDIT, i, ResourceBundles.getGuiBundle().getString(
                    "credit"), cellFormat));
            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "debit"), cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_CREDIT, i, ResourceBundles.getGuiBundle().getString(
                    "credit"), cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "debit"), cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_CREDIT, i, ResourceBundles.getGuiBundle().getString(
                    "credit"), cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "debit"), cellFormat));
            i++;

            turnoverSheetTableDTOs = turnoverSheetTableDTOs.stream().sorted((o1, o2) -> {
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


            for (TurnoverSheetTableDTO turnoverSheetTableDTO : turnoverSheetTableDTOs) {
                sheet.addCell(new Number(SUBSCRIBER_ID_COLUMN, i, turnoverSheetTableDTO.getSubscriberAccount()));
                sheet.addCell(new Label(SUBSCRIBER_ADDRESS_COLUMN, i, getFullSubscriberAddress(turnoverSheetTableDTO)));
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, getAbbreviatedName(turnoverSheetTableDTO)));
                if (isAdditional) {
                    sheet.addCell(new Number(SERVICE_COLUMN, i, turnoverSheetTableDTO.getServiceId()));
                }
                sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_CREDIT, i, turnoverSheetTableDTO.getBroughtForwardBalanceCredit()));
                sheet.addCell(new Number(BROUGHT_FORWARD_BALANCE_DEBIT, i, turnoverSheetTableDTO.getBroughtForwardBalanceDebit()));
                sheet.addCell(new Number(TURNOVER_BALANCE_CREDIT, i, turnoverSheetTableDTO.getTurnoverBalanceCredit()));
                sheet.addCell(new Number(TURNOVER_BALANCE_DEBIT, i, turnoverSheetTableDTO.getTurnoverBalanceDebit()));
                sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_CREDIT, i, turnoverSheetTableDTO.getCarriedForwardBalanceCredit()));
                sheet.addCell(new Number(CARRIED_FORWARD_BALANCE_DEBIT, i, turnoverSheetTableDTO.getCarriedForwardBalanceDebit()));
                i++;
            }

            if (isAdditional) {
                for (Service additionalService : additionalServices) {
                    Long broughtForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == additionalService.getId()).mapToLong(value -> value.getBroughtForwardBalanceCredit()).sum();
                    Long broughtForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == additionalService.getId()).mapToLong(value -> value.getBroughtForwardBalanceDebit()).sum();
                    Long carriedForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == additionalService.getId()).mapToLong(value -> value.getCarriedForwardBalanceCredit()).sum();
                    Long carriedForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == additionalService.getId()).mapToLong(value -> value.getCarriedForwardBalanceDebit()).sum();
                    Long turnoverBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == additionalService.getId()).mapToLong(value -> value.getTurnoverBalanceCredit()).sum();
                    Long turnoverBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == additionalService.getId()).mapToLong(value -> value.getTurnoverBalanceDebit()).sum();

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
            Long broughtForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().mapToLong(value -> value.getBroughtForwardBalanceCredit()).sum();
            Long broughtForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().mapToLong(value -> value.getBroughtForwardBalanceDebit()).sum();
            Long carriedForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().mapToLong(value -> value.getCarriedForwardBalanceCredit()).sum();
            Long carriedForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().mapToLong(value -> value.getCarriedForwardBalanceDebit()).sum();
            Long turnoverBalanceCreditSum = turnoverSheetTableDTOs.stream().mapToLong(value -> value.getTurnoverBalanceCredit()).sum();
            Long turnoverBalanceDebitSum = turnoverSheetTableDTOs.stream().mapToLong(value -> value.getTurnoverBalanceDebit()).sum();


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
