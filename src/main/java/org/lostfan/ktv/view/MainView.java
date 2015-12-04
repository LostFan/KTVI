package org.lostfan.ktv.view;

import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class MainView {

    public class MenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = ((JMenuItem) e.getSource()).getName();
            if (MainView.this.menuActionListener != null) {
                MainView.this.menuActionListener.actionPerformed(name);
            }
        }
    }

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private JFrame frame;
    private JPanel contentPanel;
    private ViewActionListener menuActionListener;

    public MainView(MainModel model) {
        this.frame = new JFrame("KTV");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        this.frame.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu(getGuiString("menu.file"));
        JMenu entityMenu = new JMenu(getGuiString("menu.entities"));
        JMenu documentMenu = new JMenu(getGuiString("menu.documents"));
        JMenu reportMenu = new JMenu(getGuiString("menu.reports"));
        JMenu serviceMenu = new JMenu(getGuiString("menu.services"));
        menuBar.add(fileMenu);
        menuBar.add(entityMenu);
        menuBar.add(documentMenu);
        menuBar.add(serviceMenu);
        menuBar.add(reportMenu);

        JMenuItem exitMenuItem = new JMenuItem(getGuiString("menu.file.exit"));
        exitMenuItem.addActionListener(e ->
            MainView.this.frame.dispatchEvent(new WindowEvent( MainView.this.frame, WindowEvent.WINDOW_CLOSING)));
        fileMenu.add(exitMenuItem);

        MenuActionListener menuActionListener = new MenuActionListener();
        for (String code : model.getEntityItems()) {
            JMenuItem entityMenuItem = new JMenuItem(getEntityString(code));
            entityMenuItem.setName(code);
            entityMenu.add(entityMenuItem);
            entityMenuItem.addActionListener(menuActionListener);
        }

        for (String code : model.getDocumentItems()) {
            JMenuItem entityMenuItem = new JMenuItem(getEntityString(code));
            entityMenuItem.setName(code);
            documentMenu.add(entityMenuItem);
            entityMenuItem.addActionListener(menuActionListener);
        }

        for (String code : model.getServicesItems()) {
            JMenuItem entityMenuItem = new JMenuItem(getEntityString(code));
            entityMenuItem.setName(code);
            serviceMenu.add(entityMenuItem);
            entityMenuItem.addActionListener(menuActionListener);
        }


        model.addObserver(args -> {
            frame.setTitle("KTV - " + getEntityString(model.getCurrentModel().getEntityNameKey()));
        });

        buildLayout();
        frame.setVisible(true);
    }

    private void buildLayout() {
        this.frame.setSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setLocationRelativeTo(null);

        this.contentPanel = new JPanel(new BorderLayout(10, 10));
        this.contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.frame.add(this.contentPanel);
    }

    public void setTableView(EntityTableView view) {
        this.contentPanel.removeAll();
        this.contentPanel.add(view.getContentPanel());
        this.frame.revalidate();
        this.frame.repaint();
    }

    public void setMenuActionListener(ViewActionListener menuActionListener) {
        this.menuActionListener = menuActionListener;
    }

    public String getEntityString(String key) {
        return ResourceBundles.getEntityBundle().getString(key);
    }

    public String getGuiString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
