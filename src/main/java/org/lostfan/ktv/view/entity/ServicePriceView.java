package org.lostfan.ktv.view.entity;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.model.dto.ServiceWithPrices;
import org.lostfan.ktv.utils.DateFormatter;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.FormView;

public class ServicePriceView extends FormView {

    private static class ArchiveTableModel extends AbstractTableModel {

        private List<ServicePrice> prices;

        public ArchiveTableModel(List<ServicePrice> prices) {
            this.prices = prices;
        }

        @Override
        public int getRowCount() {
            return prices.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ServicePrice servicePrice = prices.get(rowIndex);
            if (columnIndex == 0) {
                return servicePrice.getPrice();
            } else {
                return DateFormatter.format(servicePrice.getDate());
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            return ResourceBundles.getEntityBundle().getString(columnIndex == 0 ? "servicePrice.price" : "servicePrice.date");
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Integer.class : LocalDate.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    private ServiceWithPrices service;
    private ViewActionListener saveActionListener;
    private ViewActionListener deleteActionListener;

    private JTable archiveTable;
    private BigDecimalFormField priceField;
    private DateFormField dateField;
    private String newPriceTitle;

    private JButton saveButton;
    private JButton deleteButton;
    private JButton cancelButton;

    public ServicePriceView(ServiceWithPrices service) {
        super();
        setTitle(getGuiString("servicePrice.windowTitle"));
        this.service = service;

        this.archiveTable = new JTable(new ArchiveTableModel(service.getArchivePrices()));
        // Align center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        this.archiveTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.archiveTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        this.priceField = new BigDecimalFormField("servicePrice.price");
        this.dateField = new DateFormField("servicePrice.date");

        if (service.getNewPrice() != null) {
            this.priceField.setValue(service.getNewPrice().getPrice());
            this.dateField.setValue(service.getNewPrice().getDate());
            newPriceTitle = getGuiString("servicePrice.changeNew") + ":";
        } else {
            this.priceField.setValue(BigDecimal.ZERO);
            newPriceTitle = getGuiString("servicePrice.createNew") + ":";
        }
        String nameSaveButton = "buttons.save";
        if(service.getNewPrice() != null) {
            nameSaveButton = "buttons.change";
        }
        this.saveButton = new JButton(getGuiString(nameSaveButton));
        this.saveButton.addActionListener(e -> {
            if (saveActionListener != null) {
                saveActionListener.actionPerformed(buildNewServicePrice());
            }
        });

        this.deleteButton = new JButton(getGuiString("buttons.delete"));
        this.deleteButton.addActionListener(e -> {
            if (deleteActionListener != null) {
                deleteActionListener.actionPerformed(service.getNewPrice());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> hide());

        buildLayout();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout());
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel mainPanel = new JPanel();
        BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
        mainPanel.setLayout(layout);
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        getContentPanel().add(mainPanel, BorderLayout.CENTER);

        Box box = Box.createHorizontalBox();
        box.add(new JLabel(getGuiString("servicePrice.currentPrice") + ": "));
        box.add(Box.createRigidArea(new Dimension(15, 0)));
        if (service.getCurrentPrice() == null) {
            box.add(new JLabel("-"));
        } else {
            String priceStr = String.format("%.2f (%s)",service.getCurrentPrice().getPrice(),
                    DateFormatter.format(service.getCurrentPrice().getDate()));
            box.add(new JLabel(priceStr));
        }
        mainPanel.add(box);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (service.getArchivePrices().size() != 0) {
            mainPanel.add(new JLabel(getGuiString("servicePrice.archive") + ":"));
            mainPanel.add(new JScrollPane(this.archiveTable));
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        setFormTitle(newPriceTitle);
        addFormField(this.priceField);
        addFormField(this.dateField);
        mainPanel.add(getFieldPanel(), BorderLayout.PAGE_END);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        getContentPanel().add(buttonPanel, BorderLayout.PAGE_END);

        buttonPanel.add(this.saveButton);
        if(service.getNewPrice() != null) {
            buttonPanel.add(this.deleteButton);
        }
        buttonPanel.add(this.cancelButton);
    }

    public ServicePrice buildNewServicePrice() {
        ServicePrice servicePrice = new ServicePrice();
        servicePrice.setServiceId(this.service.getId());
        servicePrice.setPrice(this.priceField.getValue() == null ? BigDecimal.ZERO : this.priceField.getValue());
        servicePrice.setDate(this.dateField.getValue());
        return servicePrice;
    }

    public void setSaveActionListener(ViewActionListener saveActionListener) {
        this.saveActionListener = saveActionListener;
    }

    public void setDeleteActionListener(ViewActionListener deleteActionListener) {
        this.deleteActionListener = deleteActionListener;
    }
}
