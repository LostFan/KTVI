package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.model.BaseEntityModel;
import org.lostfan.ktv.utils.Observer;
import org.lostfan.ktv.utils.ResourceBundles;

public class EntityTableView {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntityTableView.this.revalidate();
        }
    }

    private JPanel contentPanel;
    private JTable table;
    private JScrollPane tableScrollPane;
    private JButton findButton;
    private JButton addButton;
    private JButton changeButton;
    private JButton deleteButton;

    private ModelObserver modelObserver;

    private BaseEntityModel model;

    public EntityTableView(BaseEntityModel model) {
        this.model = model;

        this.table = new JTable(model.getTableModel());
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setAutoCreateRowSorter(true);
        this.table.setFillsViewportHeight(true);

        this.findButton = new JButton(getString("buttons.find"));
        this.addButton = new JButton(getString("buttons.add"));
        this.changeButton = new JButton(getString("buttons.changeSelected"));
        this.deleteButton = new JButton(getString("buttons.delete"));

        buildLayout();

        this.modelObserver = new ModelObserver();

        model.addObserver(this.modelObserver);
    }

    private void buildLayout() {
        this.contentPanel = new JPanel(new BorderLayout(10, 10));

        // ID column values should be aligned to the left;
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        this.tableScrollPane = new JScrollPane(this.table);

        this.contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.contentPanel.add(rightPanel, BorderLayout.LINE_END);

        JPanel rightPanelInner = new JPanel(new GridLayout(4, 1, 0, 10));
        rightPanel.add(rightPanelInner);

        rightPanelInner.add(this.findButton);
        rightPanelInner.add(this.addButton);
        rightPanelInner.add(this.changeButton);
        rightPanelInner.add(this.deleteButton);
    }

    public int getSelectedIndex() {
        return this.table.getSelectedRow();
    }

    public int[] getSelectedIndexes() {
        return this.table.getSelectedRows();
    }


    public JPanel getContentPanel() {
        return this.contentPanel;
    }

    public boolean isConfirm() {
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        Object[] selValues = {ResourceBundles.getGuiBundle().getString("buttons.yes"),
                ResourceBundles.getGuiBundle().getString("buttons.cancel") };
        String message = ResourceBundles.getGuiBundle().getString("window.delete") + " : "
                + ResourceBundles.getEntityBundle().getString(model.getEntityNameKey());
        int result = JOptionPane.showOptionDialog(null,
                ResourceBundles.getGuiBundle().getString("message.deleteQuestion"), message,
                optionType, messageType, null, selValues,
                selValues[0]);

        return result == 0 ? true : false;
    }

    public void setModel(BaseEntityModel model) {
        this.model.removeObserver(modelObserver);
        this.model = model;
        model.addObserver(this.modelObserver);

        this.table.setModel(model.getTableModel());
        this.tableScrollPane.setViewportView(this.table);

        revalidate();
    }

    private void revalidate() {
        this.contentPanel.invalidate();
        this.contentPanel.repaint();
    }

    public void addFindActionListener(ActionListener listener) {
        this.findButton.addActionListener(listener);
    }

    public void addAddActionListener(ActionListener listener) {
        this.addButton.addActionListener(listener);
    }

    public void addChangeActionListener(ActionListener listener) {
        this.changeButton.addActionListener(listener);
    }

    public void addDoubleClickListener(MouseListener listener) {
        this.table.addMouseListener(listener);
    }

    public void addDeleteActionListener(ActionListener listener) {
        this.deleteButton.addActionListener(listener);
    }

    private String getString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
