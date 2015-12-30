package org.lostfan.ktv.view;

import javax.swing.*;
import java.awt.*;

public abstract class FrameView extends View {

    private JFrame frame;

    public FrameView() {
        this.frame = new JFrame();
        this.frame.add(getContentPanel());
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public FrameView(String title) {
        this();
        setTitle(title);
    }

    public JFrame getFrame() {
        return this.frame;
    }

    protected void setSize(int width, int height) {
        this.frame.setSize(new Dimension(width, height));
    }

    protected void setTitle(String title) {
        this.frame.setTitle(title);
    }

    protected void revalidate() {
        this.frame.revalidate();
        this.frame.repaint();
    }

    public void show() {
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public void hide() {
        this.frame.setVisible(false);
    }

    public boolean isVisible() {
        return this.frame.isVisible();
    }

    /**
     * Opens the specified view as a modal dialog frame.
     * Blocks the current thread until the dialog returns a result
     * @param dialogView A dialog view for opening
     */
    public void openDialog(DialogView dialogView) {
        dialogView.setOwner(this.frame);
    }
}
