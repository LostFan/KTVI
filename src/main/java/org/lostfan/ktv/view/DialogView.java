package org.lostfan.ktv.view;

import javax.swing.*;
import java.awt.*;

/**
 * Represent a modal window view.
 * @param <T> The type of the result of this dialog.
 */
public abstract class DialogView<T> extends View {

    /**
     * HACK: for opening dialog windows without a frame
     */
    public static void open(DialogView view) {
        view.setOwner(new JFrame());
    }

    private JDialog dialog;
    private String title;
    private T result;
    private Dimension size;

    public void setTitle(String title) {
        this.title = title;
        if (this.dialog != null) {
            this.dialog.setTitle(title);
        }
    }

    protected void setSize(int width, int height) {
        this.size = new Dimension(width, height);
        setDialogSize(size);
    }

    private void setDialogSize(Dimension size) {
        if (this.dialog != null && this.size != null) {
            this.dialog.setSize(size);
        }
    }

    void setOwner(JFrame owner) {
        this.dialog = new JDialog(owner, title, true);
        this.dialog.add(getContentPanel());
        setDialogSize(size);
        this.dialog.setLocationRelativeTo(null);
        this.dialog.setVisible(true);
    }

    /**
     * Sets the dialog result, hides the dialog frame and restores control of the main thread
     * @param result The result of this dialog
     */
    protected void set(T result) {
        this.result = result;
        this.dialog.setVisible(false);
    }

    /**
     * Returns the dialog result or null if the result has not been specified.
     */
    public T get() {
        return this.result;
    }

}
