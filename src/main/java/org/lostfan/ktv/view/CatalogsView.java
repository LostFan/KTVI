package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class CatalogsView {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private JFrame frame;
    private JButton serviceButton;
    private JButton subscriberButton;

    public CatalogsView() {
        this.frame = new JFrame("Справочники");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.serviceButton = new JButton("Услуги");
        this.subscriberButton = new JButton("Абоненты");

        buildLayout();
        frame.setVisible(true);
    }

    private void buildLayout() {
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ID column values should be aligned to the left;
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        frame.add(rightPanel, BorderLayout.CENTER);

        JPanel rightPanelInner = new JPanel(new GridLayout(2, 1, 0, 10));
        rightPanel.add(rightPanelInner);

        rightPanelInner.add(this.serviceButton);
        rightPanelInner.add(this.subscriberButton);
    }

    public void addServiceActionListener(ActionListener listener) {
        this.serviceButton.addActionListener(listener);
    }

    public void addSubscriberActionListener(ActionListener listener) {
        this.subscriberButton.addActionListener(listener);
    }

}
