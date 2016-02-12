package org.lostfan.ktv.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.lostfan.ktv.utils.FormSize;

public abstract class FrameView extends View {

    public static final int WIDTH = 750;
    public static final int HEIGHT = 500;

    private JFrame frame;

    public FrameView() {
        this.frame = new JFrame();
        JScrollPane tableScrollPane = new JScrollPane(getContentPanel());
        tableScrollPane.setBorder(null);
        this.frame.add(tableScrollPane);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                super.windowDeactivated(e);
                if(FrameView.this.frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                    FormSize.saveFormSize(getTitle(), getWidth(), getHeight());
                }

            }
        });
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

    protected void setSize() {
        FormSize formSize = FormSize.getFormSize(getTitle());
        if(formSize != null) {
            setSize(formSize.getWidth(), formSize.getHeight());
        } else {
            setSize(WIDTH, HEIGHT);
        }
    }

    protected int getWidth() {
        return this.frame.getWidth();
    }

    protected int getHeight() {
        return this.frame.getHeight();
    }

    protected void setTitle(String title) {
        this.frame.setTitle(title);
    }

    protected String getTitle() {
        return this.frame.getTitle();
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
