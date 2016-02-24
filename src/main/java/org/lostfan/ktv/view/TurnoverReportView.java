package org.lostfan.ktv.view;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.*;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.TurnoverReportModel;
import org.lostfan.ktv.model.dto.TurnoverSheetTableDTO;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.components.EntityPanel;
import org.lostfan.ktv.view.components.EntityPanelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 1 on 23.01.2016.
 */
public class TurnoverReportView extends FormView {

    private class ReportTableModel extends AbstractTableModel {

        private List<TurnoverSheetTableDTO> turnoverSheetTableDTOs = new ArrayList<>();

        public ReportTableModel() {
        }

        public ReportTableModel(List<TurnoverSheetTableDTO> turnoverSheetTableDTOs) {
            this.turnoverSheetTableDTOs = turnoverSheetTableDTOs;
        }

        @Override
        public int getRowCount() {
            return turnoverSheetTableDTOs.size();
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: return this.turnoverSheetTableDTOs.get(rowIndex).getSubscriberAccount();
                case 1: return this.turnoverSheetTableDTOs.get(rowIndex).getBroughtForwardBalanceCredit();
                case 2: return this.turnoverSheetTableDTOs.get(rowIndex).getBroughtForwardBalanceDebit();
                case 3: return this.turnoverSheetTableDTOs.get(rowIndex).getTurnoverBalanceCredit();
                case 4: return this.turnoverSheetTableDTOs.get(rowIndex).getTurnoverBalanceDebit();
                case 5: return this.turnoverSheetTableDTOs.get(rowIndex).getCarriedForwardBalanceCredit();
                case 6: return this.turnoverSheetTableDTOs.get(rowIndex).getCarriedForwardBalanceDebit();
            }

            return null;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: return ResourceBundles.getEntityBundle().getString(
                        "subscriber");
                case 1: return ResourceBundles.getGuiBundle().getString(
                        "broughtForwardBalanceCredit");
                case 2: return ResourceBundles.getGuiBundle().getString(
                        "broughtForwardBalanceDebit");
                case 3: return ResourceBundles.getGuiBundle().getString(
                        "turnoverBalanceCredit");
                case 4: return ResourceBundles.getGuiBundle().getString(
                        "turnoverBalanceDebit");
                case 5: return ResourceBundles.getGuiBundle().getString(
                        "carriedForwardBalanceCredit");
                case 6: return ResourceBundles.getGuiBundle().getString(
                        "carriedForwardBalanceDebit");
            }

            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Integer.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    private class ServiceFormField extends FormField<Integer> {

        private EntityPanel panel;

        public ServiceFormField(String fieldKey) {
            super(fieldKey);
            this.panel = EntityPanelFactory.createEntityPanel(EntityFieldTypes.Service);

            this.panel.setParentView(TurnoverReportView.this);
        }

        @Override
        public JComponent getInputComponent() {
            return panel;
        }

        @Override
        public Integer getValue() {
            return this.panel.getSelectedEntity() != null ? this.panel.getSelectedEntity().getId() : null;
        }

        @Override
        public void setValue(Integer value) {

        }
    }


    private JButton addButton;
    private JButton cancelButton;
    private JButton excelButton;
    private JTable reportTable;

    private TurnoverReportModel model;

    private ViewActionListener addActionListener;
    private ViewActionListener cancelActionListener;
    protected ViewActionListener changeActionListener;

    private DateFormField dateField;
    private BooleanFormField isAdditionalField;
    private ServiceFormField serviceField;

    private Entity entity;
    private Map<EntityField, FormView.FormField> entityFormFieldMap;

    public TurnoverReportView(TurnoverReportModel model) {
        this(model, null);
    }

    public TurnoverReportView(TurnoverReportModel model, Entity entity) {
        this.entity = entity;
        this.model = model;
        reportTable = new JTable(new ReportTableModel());
        this.reportTable.setPreferredScrollableViewportSize(new Dimension(500, 70));

        setTitle(getEntityString(model.getEntityNameKey()));

        dateField = new DateFormField("renderedService.date");
        addFormField(dateField);
        isAdditionalField = new BooleanFormField("service.additional");
        addFormField(isAdditionalField);
        serviceField = new ServiceFormField("service");
        addFormField(serviceField);

        entityFormFieldMap = new HashMap<>();

        this.addButton = new JButton(getGuiString("buttons.shape"));
        this.addButton.addActionListener(e -> {
            ReportTableModel reportTableModel;
            if(isAdditionalField.getValue()) {
                reportTableModel = new ReportTableModel(
                        model.getTurnoverSheetDataByAdditionalServices(dateField.getValue()));
            } else {
                reportTableModel = new ReportTableModel(
                        model.getTurnoverSheetData(dateField.getValue(), serviceField.getValue()));
            }
            reportTable.setModel(reportTableModel);
            reportTable.repaint();
            if (this.addActionListener != null) {

            }
        });
        this.excelButton = new JButton(getGuiString("buttons.shapeExcel"));
        this.excelButton.addActionListener(e -> {
            if(dateField.getValue() == null) {
                dateField.setError("errors.empty");
                return;
            }
            dateField.clearError();
            if(!isAdditionalField.getValue() && serviceField.getValue() == null) {
                serviceField.setError("errors.empty");
                return;
            }
            serviceField.clearError();
            generateExcel();
        });



        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            if (this.cancelActionListener != null) {
                this.cancelActionListener.actionPerformed(null);
            }
            hide();
        });

        for (EntityField entityField : model.getFields()) {
            if (!entityField.isEditable()) {
                continue;
            }

        }



        this.isAdditionalField.addValueListener(newValue -> {
            if ((isAdditionalField.getValue())) {
                serviceField.setVisible(false);
            } else {
                serviceField.setVisible(true);
            }
        });

        buildLayout();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.reportTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
//        addStringActionTableCellEditorToColumns();
        JScrollPane tableScrollPane = new JScrollPane(this.reportTable);

        getContentPanel().add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(excelButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generateExcel() {
        WritableWorkbook workbook;
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

            if(isAdditionalField.getValue()) {
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
            if(isAdditionalField.getValue()) {
                fileName = ResourceBundles.getGuiBundle().getString(
                        "additionalServices") + " "
                        + dateField.getValue().getMonthValue() + "-" + dateField.getValue().getYear();
            } else {
                fileName = model.getService(serviceField.getValue()).toString() + " "
                        + dateField.getValue().getMonthValue() + "-" + dateField.getValue().getYear();
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
            sheet.mergeCells(TURNOVER_BALANCE_DEBIT, i ,TURNOVER_BALANCE_CREDIT, i);
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
                    "subscriber"),cellFormat));
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_ADDRESS_COLUMN, i, ResourceBundles.getGuiBundle().getString(
                    "address"), cellFormat));
            sheet.addCell(new jxl.write.Label(SUBSCRIBER_NAME_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                    "subscriber.name"),cellFormat));
            if(isAdditionalField.getValue()) {
                sheet.addCell(new jxl.write.Label(SERVICE_COLUMN, i, ResourceBundles.getEntityBundle().getString(
                        "service"),cellFormat));
            }
            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_CREDIT, i, ResourceBundles.getGuiBundle().getString(
                    "credit"),cellFormat));
            sheet.addCell(new jxl.write.Label(BROUGHT_FORWARD_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "debit"),cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_CREDIT, i, ResourceBundles.getGuiBundle().getString(
                    "credit"),cellFormat));
            sheet.addCell(new jxl.write.Label(TURNOVER_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "debit"),cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_CREDIT, i, ResourceBundles.getGuiBundle().getString(
                    "credit"),cellFormat));
            sheet.addCell(new jxl.write.Label(CARRIED_FORWARD_BALANCE_DEBIT, i, ResourceBundles.getGuiBundle().getString(
                    "debit"),cellFormat));
            i++;

            List<TurnoverSheetTableDTO> turnoverSheetTableDTOs;
            if(isAdditionalField.getValue()) {
                turnoverSheetTableDTOs = model.getTurnoverSheetDataByAdditionalServices(dateField.getValue());
            } else {
                turnoverSheetTableDTOs = model.getTurnoverSheetData(dateField.getValue(), serviceField.getValue());
            }

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
                turnoverSheetTableDTO.getAbbreviatedName();
                sheet.addCell(new Number(SUBSCRIBER_ID_COLUMN, i, turnoverSheetTableDTO.getSubscriberAccount()));
                sheet.addCell(new Label(SUBSCRIBER_ADDRESS_COLUMN, i, turnoverSheetTableDTO.getFullSubscriberAddress()));
                sheet.addCell(new Label(SUBSCRIBER_NAME_COLUMN, i, turnoverSheetTableDTO.getAbbreviatedName()));
                if(isAdditionalField.getValue()) {
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

            if(isAdditionalField.getValue()) {
                List <Service> services = model.getAllServices().stream().filter(e -> e.isAdditionalService()).collect(Collectors.toList());
                for (Service service : services) {
                    Long broughtForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == service.getId()).mapToLong(value -> value.getBroughtForwardBalanceCredit()).sum();
                    Long broughtForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == service.getId()).mapToLong(value -> value.getBroughtForwardBalanceDebit()).sum();
                    Long carriedForwardBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == service.getId()).mapToLong(value -> value.getCarriedForwardBalanceCredit()).sum();
                    Long carriedForwardBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == service.getId()).mapToLong(value -> value.getCarriedForwardBalanceDebit()).sum();
                    Long turnoverBalanceCreditSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == service.getId()).mapToLong(value -> value.getTurnoverBalanceCredit()).sum();
                    Long turnoverBalanceDebitSum = turnoverSheetTableDTOs.stream().filter(e -> e.getServiceId() == service.getId()).mapToLong(value -> value.getTurnoverBalanceDebit()).sum();

                    sheet.addCell(new Label(SUBSCRIBER_ADDRESS_COLUMN, i, service.getName()));
                    sheet.addCell(new Number(SERVICE_COLUMN, i, service.getId()));
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


//            System.out.println(cell1.getContents() + "  " + cell2.getContents() + "  " + cell3.getContents() + "  " + cell4.getContents());
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
            System.out.println("Student Details file created");
        } catch (IOException e) {
            if(e instanceof FileNotFoundException) {
                exceptionWindow(e);
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void exceptionWindow (Exception e) {
        int optionType = JOptionPane.OK_OPTION;
        int messageType = JOptionPane.WARNING_MESSAGE;
        Object[] selValues = { getGuiString("buttons.ok")};
        String message = getGuiString("message.attention");
        JOptionPane.showOptionDialog(null,
                e.getLocalizedMessage(), message,
                optionType, messageType, null, selValues,
                selValues[0]);
    }
}
