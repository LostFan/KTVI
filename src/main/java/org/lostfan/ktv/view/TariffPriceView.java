package org.lostfan.ktv.view;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.dto.TariffWithPrices;
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
                return tariffPrice.getDate();
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

    private JButton saveButton;
    private JButton cancelButton;

    public TariffPriceView(TariffWithPrices tariff) {
        this.tariff = tariff;

        this.frame = new JFrame("TarifPrice");
        this.frame.setSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setLocationRelativeTo(null);

        this.archiveTable = new JTable(new ArchiveTableModel(tariff.getArchivePrices()));

        this.priceField = new IntegerTextField();
        this.dateField = new DatePickerField();

        if (tariff.getNewPrice() != null) {
            this.priceField.setValue(tariff.getNewPrice().getPrice());
            this.dateField.setValue(tariff.getNewPrice().getDate());
        }

        this.saveButton = new JButton(ResourceBundles.getGuiBundle().getString("buttons.save"));
        this.saveButton.addActionListener(e -> {
            if (saveActionListener != null) {
                saveActionListener.actionPerformed(buildNewTariffPrice());
            }
        });

        this.cancelButton = new JButton(ResourceBundles.getGuiBundle().getString("buttons.cancel"));
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
        box.add(new JLabel("Current Price: "));
        box.add(Box.createRigidArea(new Dimension(15, 0)));
        if (tariff.getCurrentPrice() == null) {
            box.add(new JLabel("-"));
        } else {
            String priceStr = String.format("%d (%s)", tariff.getCurrentPrice().getPrice(), tariff.getCurrentPrice().getDate().toString());
            box.add(new JLabel(priceStr));
        }
        contentPanel.add(box);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (tariff.getArchivePrices().size() != 0) {
            contentPanel.add(new JLabel("Archive:"));
            contentPanel.add(new JScrollPane(this.archiveTable));
        }

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(new JLabel("Create new price:"));

        box = Box.createHorizontalBox();
        box.add(new JLabel(ResourceBundles.getEntityBundle().getString("tariffPrice.price")));
        box.add(Box.createRigidArea(new Dimension(20, 0)));
        box.add(this.priceField);
        contentPanel.add(box);

        box = Box.createHorizontalBox();
        box.add(new JLabel(ResourceBundles.getEntityBundle().getString("tariffPrice.date")));
        box.add(Box.createRigidArea(new Dimension(20, 0)));
        box.add(this.dateField);
        contentPanel.add(box);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        frame.add(buttonPanel, BorderLayout.PAGE_END);

        buttonPanel.add(this.saveButton);
        buttonPanel.add(this.cancelButton);
    }

    public TariffPrice buildNewTariffPrice() {
        TariffPrice tariffPrice = new TariffPrice();
        tariffPrice.setTariffId(this.tariff.getId());
        tariffPrice.setPrice(this.priceField.getValue());
        tariffPrice.setDate(this.dateField.getValue());
        return tariffPrice;
    }

    public void setSaveActionListener(ViewActionListener saveActionListener) {
        this.saveActionListener = saveActionListener;
    }
}
