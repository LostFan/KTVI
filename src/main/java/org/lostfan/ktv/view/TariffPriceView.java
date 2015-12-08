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
import org.lostfan.ktv.view.components.DatePickerField;
import org.lostfan.ktv.view.components.IntegerTextField;

public class TariffPriceView {

    public static final int HEIGHT = 400;
    public static final int WIDTH = 300;

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

    private JFrame frame;
    private TariffWithPrices tariff;
    private ViewActionListener saveActionListener;

    private JTable archiveTable;
    private IntegerTextField priceField;
    private DatePickerField dateField;
    private String newPriceTitle;

    private JButton saveButton;
    private JButton cancelButton;

    public TariffPriceView(TariffWithPrices tariff) {
        this.tariff = tariff;

        this.frame = new JFrame("TarifPrice");
        this.frame.setSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setLocationRelativeTo(null);

        this.archiveTable = new JTable(new ArchiveTableModel(tariff.getArchivePrices()));
        // Align center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        this.archiveTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.archiveTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        this.priceField = new IntegerTextField();
        this.dateField = new DatePickerField();

        if (tariff.getNewPrice() != null) {
            this.priceField.setValue(tariff.getNewPrice().getPrice());
            this.dateField.setValue(tariff.getNewPrice().getDate());
            newPriceTitle = getGuiString("tariffPrice.changeNew") + ":";
        } else {
            this.priceField.setValue(0);
            newPriceTitle = getGuiString("tariffPrice.createNew") + ":";
        }

        this.saveButton = new JButton(getGuiString("buttons.save"));
        this.saveButton.addActionListener(e -> {
            if (saveActionListener != null) {
                saveActionListener.actionPerformed(buildNewTariffPrice());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> frame.setVisible(false));

        buildLayout();

        this.frame.setVisible(true);
    }

    private void buildLayout() {
        frame.setLayout(new BorderLayout());
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel contentPanel = new JPanel();
        BoxLayout layout = new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS);
        contentPanel.setLayout(layout);
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        frame.add(contentPanel, BorderLayout.CENTER);

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
        contentPanel.add(box);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (tariff.getArchivePrices().size() != 0) {
            contentPanel.add(new JLabel(getGuiString("tariffPrice.archive") + ":"));
            contentPanel.add(new JScrollPane(this.archiveTable));
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        JPanel newPricePanel = new JPanel(new GridBagLayout());
        contentPanel.add(newPricePanel, BorderLayout.PAGE_END);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1d;
        c.fill=GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        newPricePanel.add(new JLabel(this.newPriceTitle), c);

        c.gridy = 1;
        c.gridwidth = 1;
        newPricePanel.add(new JLabel(getEntityString("tariffPrice.price")), c);

        c.gridx = 1;
        newPricePanel.add(this.priceField, c);

        c.gridx = 0;
        c.gridy = 2;
        newPricePanel.add(new JLabel(getEntityString("tariffPrice.date")), c);

        c.gridx = 1;
        newPricePanel.add(this.dateField, c);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        frame.add(buttonPanel, BorderLayout.PAGE_END);

        buttonPanel.add(this.saveButton);
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

    private String getGuiString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }

    private String getEntityString(String key) {
        return ResourceBundles.getEntityBundle().getString(key);
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public void hide() {
        this.frame.setVisible(false);
    }

    public void showValidationErrors(List<org.lostfan.ktv.validation.Error> errors) {
        // TODO:
    }
}
