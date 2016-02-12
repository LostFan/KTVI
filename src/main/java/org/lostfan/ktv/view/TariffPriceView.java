package org.lostfan.ktv.view;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.dto.TariffWithPrices;
import org.lostfan.ktv.utils.DateFormatter;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;

public class TariffPriceView extends FormView {

    private static class ArchiveTableModel extends AbstractTableModel {

        private List<TariffPrice> prices;

        public ArchiveTableModel(List<TariffPrice> prices) {
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
            TariffPrice tariffPrice = prices.get(rowIndex);
            if (columnIndex == 0) {
                return tariffPrice.getPrice();
            } else {
                return DateFormatter.format(tariffPrice.getDate());
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            return ResourceBundles.getEntityBundle().getString(columnIndex == 0 ? "tariffPrice.price" : "tariffPrice.date");
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

    private TariffWithPrices tariff;
    private ViewActionListener saveActionListener;
    private ViewActionListener deleteActionListener;

    private JTable archiveTable;
    private IntegerFormField priceField;
    private DateFormField dateField;
    private String newPriceTitle;

    private JButton saveButton;
    private JButton deleteButton;
    private JButton cancelButton;

    public TariffPriceView(TariffWithPrices tariff) {
        super();
        setTitle(getGuiString("tariffPrice.windowTitle"));
        this.tariff = tariff;

        setSize();

        this.archiveTable = new JTable(new ArchiveTableModel(tariff.getArchivePrices()));
        // Align center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        this.archiveTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.archiveTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        this.priceField = new IntegerFormField("tariffPrice.price");
        this.dateField = new DateFormField("tariffPrice.date");

        if (tariff.getNewPrice() != null) {
            this.priceField.setValue(tariff.getNewPrice().getPrice());
            this.dateField.setValue(tariff.getNewPrice().getDate());
            newPriceTitle = getGuiString("tariffPrice.changeNew") + ":";
        } else {
            this.priceField.setValue(0);
            newPriceTitle = getGuiString("tariffPrice.createNew") + ":";
        }
        String nameSaveButton = "buttons.save";
        if(tariff.getNewPrice() != null) {
            nameSaveButton = "buttons.change";
        }
        this.saveButton = new JButton(getGuiString(nameSaveButton));
        this.saveButton.addActionListener(e -> {
            if (saveActionListener != null) {
                saveActionListener.actionPerformed(buildNewTariffPrice());
            }
        });

        this.deleteButton = new JButton(getGuiString("buttons.delete"));
        this.deleteButton.addActionListener(e -> {
            if (deleteActionListener != null) {
                deleteActionListener.actionPerformed(tariff.getNewPrice());
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
        box.add(new JLabel(getGuiString("tariffPrice.currentPrice") + ": "));
        box.add(Box.createRigidArea(new Dimension(15, 0)));
        if (tariff.getCurrentPrice() == null) {
            box.add(new JLabel("-"));
        } else {
            String priceStr = String.format("%d (%s)",tariff.getCurrentPrice().getPrice(),
                    DateFormatter.format(tariff.getCurrentPrice().getDate()));
            box.add(new JLabel(priceStr));
        }
        mainPanel.add(box);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (tariff.getArchivePrices().size() != 0) {
            mainPanel.add(new JLabel(getGuiString("tariffPrice.archive") + ":"));
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
        if(tariff.getNewPrice() != null) {
            buttonPanel.add(this.deleteButton);
        }
        buttonPanel.add(this.cancelButton);
    }

    public TariffPrice buildNewTariffPrice() {
        TariffPrice tariffPrice = new TariffPrice();
        tariffPrice.setTariffId(this.tariff.getId());
        tariffPrice.setPrice(this.priceField.getValue() == null ? 0 : this.priceField.getValue());
        tariffPrice.setDate(this.dateField.getValue());
        return tariffPrice;
    }

    public void setSaveActionListener(ViewActionListener saveActionListener) {
        this.saveActionListener = saveActionListener;
    }

    public void setDeleteActionListener(ViewActionListener deleteActionListener) {
        this.deleteActionListener = deleteActionListener;
    }
}
