package org.lostfan.ktv.view;

import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class MainView extends FrameView {

    private class MenuActionListener implements ActionListener {

        private boolean isEntityMenu;

        public MenuActionListener(boolean isEntityMenu) {
            this.isEntityMenu = isEntityMenu;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = ((JMenuItem) e.getSource()).getName();
            if (this.isEntityMenu && MainView.this.menuEntityActionListener != null) {
                MainView.this.menuEntityActionListener.actionPerformed(name);
            }

            if (!this.isEntityMenu && MainView.this.menuServiceActionListener != null) {
                MainView.this.menuServiceActionListener.actionPerformed(name);
            }
        }
    }

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private ViewActionListener menuEntityActionListener;
    private ViewActionListener menuServiceActionListener;

    public MainView(MainModel model) {
        super("KTV");
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        getFrame().setJMenuBar(menuBar);
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
            MainView.this.getFrame().dispatchEvent(new WindowEvent(MainView.this.getFrame(), WindowEvent.WINDOW_CLOSING)));
        fileMenu.add(exitMenuItem);

        MenuActionListener menuActionListener = new MenuActionListener(true);
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

        menuActionListener = new MenuActionListener(false);
        for (String code : model.getServicesItems()) {
            JMenuItem entityMenuItem = new JMenuItem(getEntityString(code));
            entityMenuItem.setName(code);
            serviceMenu.add(entityMenuItem);
            entityMenuItem.addActionListener(menuActionListener);
        }

        model.addObserver(args -> {
            setTitle("KTV - " + getEntityString(model.getCurrentModel().getEntityNameKey()));
        });

        buildLayout();
        show();
    }

    private void buildLayout() {
        setSize(WIDTH, HEIGHT);

        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    public void setTableView(EntityTableView view) {
        getContentPanel().removeAll();
        getContentPanel().add(view.getContentPanel());
        revalidate();
    }

    public void setMenuEntityActionListener(ViewActionListener menuEntityActionListener) {
        this.menuEntityActionListener = menuEntityActionListener;
    }

    public void setMenuServiceActionListener(ViewActionListener menuServiceActionListener) {
        this.menuServiceActionListener = menuServiceActionListener;
    }
}
