package org.lostfan.ktv.view;

import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.utils.ResourceBundles;

import java.awt.*;
import javax.swing.*;

public class MainView {

    public interface EntityModelListener {
        void entityChanged(EntityModel newModel);
    }

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private JFrame frame;
    private JMenuBar menuBar;
    private JPanel contentPanel;
    private EntityModelListener entityModelListener;

    public MainView(MainModel model) {
        this.frame = new JFrame("KTV");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menuBar = new JMenuBar();
        this.frame.setJMenuBar(this.menuBar);
        JMenu fileMenu = new JMenu(getGuiString("menu.file"));
        JMenu entityMenu = new JMenu(getGuiString("menu.entities"));
        JMenu documentMenu = new JMenu(getGuiString("menu.documents"));
        JMenu reportMenu = new JMenu(getGuiString("menu.reports"));
        menuBar.add(fileMenu);
        menuBar.add(entityMenu);
        menuBar.add(documentMenu);
        menuBar.add(reportMenu);

        for (EntityModel entityModel : model.getEntityModels()) {
            JMenuItem entityMenuItem = new JMenuItem(getEntityString(entityModel.getEntityNameKey()));
            entityMenu.add(entityMenuItem);
            entityMenuItem.addActionListener(e -> {
                if (entityModelListener != null)
                    entityModelListener.entityChanged(entityModel);
            });
        }
        for (EntityModel entityModel : model.getDocumentModels()) {
            JMenuItem entityMenuItem = new JMenuItem(getEntityString(entityModel.getEntityNameKey()));
            documentMenu.add(entityMenuItem);
            entityMenuItem.addActionListener(e -> {
                if (entityModelListener != null)
                    entityModelListener.entityChanged(entityModel);
            });
        }


        model.addObserver(args -> {
            setContentPanel(model.getContentPanel());
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

    public void setContentPanel(JPanel panel) {
        this.contentPanel.removeAll();
        this.contentPanel.add(panel);
        this.frame.revalidate();
        this.frame.repaint();
    }

    public void setEntityModelListener(EntityModelListener listener) {
        this.entityModelListener = listener;
    }

    public String getEntityString(String key) {
        return ResourceBundles.getEntityBundle().getString(key);
    }

    public String getGuiString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
