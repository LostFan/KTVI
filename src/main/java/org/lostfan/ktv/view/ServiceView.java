package org.lostfan.ktv.view;

import org.lostfan.ktv.model.ServiceModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class ServiceView {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;

    private JFrame frame;
    private JTable table;
    private JButton findButton;
    private JButton addButton;
    private JButton changeButton;
    private JButton deleteButton;

    public ServiceView(ServiceModel model) {
        this.frame = new JFrame("Services");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.table = new JTable(model.getServiceTableModel());
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setFillsViewportHeight(true);

        this.findButton = new JButton("Find");
        this.addButton = new JButton("Add");
        this.changeButton = new JButton("Change Selected");
        this.deleteButton = new JButton("Delete");

        buildLayout();
        frame.setVisible(true);
    }

    private void buildLayout() {
        frame.setSize(new Dimension(WIDTH, HEIGHT));

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ID column values should be aligned to the left;
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);

        this.frame.add(new JScrollPane(this.table), BorderLayout.LINE_START);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        frame.add(rightPanel, BorderLayout.LINE_END);

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

    public void addFindActionListener(ActionListener listener) {
        this.findButton.addActionListener(listener);
    }

    public void addAddActionListener(ActionListener listener) {
        this.addButton.addActionListener(listener);
    }

    public void addChangeActionListener(ActionListener listener) {
        this.changeButton.addActionListener(listener);
    }

    public void addDeleteActionListener(ActionListener listener) {
        this.deleteButton.addActionListener(listener);
    }
}
