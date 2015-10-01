package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Created by Ihar_Niakhlebau on 30-Sep-15.
 */
public class MainView {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private JFrame frame;
    private JButton documentButton;
    private JButton reportButton;

    public MainView() {
        this.frame = new JFrame("Главное меню");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.documentButton = new JButton("Документы");
        this.reportButton = new JButton("Отчеты");

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

        rightPanelInner.add(this.documentButton);
        rightPanelInner.add(this.reportButton);
    }

    public void addDocumentActionListener(ActionListener listener) {
        this.documentButton.addActionListener(listener);
    }

    public void addReportActionListener(ActionListener listener) {
        this.reportButton.addActionListener(listener);
    }

}
