package org.lostfan.ktv.view;

import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.*;

public abstract class View {

    private JPanel contentPanel;

    public View() {
        this.contentPanel = new JPanel();
    }

    protected void revalidate() {
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public static String getEntityString(String key) {
        return ResourceBundles.getEntityBundle().getString(key);
    }

    public static String getGuiString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
