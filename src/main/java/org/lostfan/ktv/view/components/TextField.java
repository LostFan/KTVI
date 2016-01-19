package org.lostfan.ktv.view.components;

import org.lostfan.ktv.utils.DefaultContextMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * A custom JTextField implementation.
 * Supports tracking the confirmed change of the value (Focus lost or "Enter" key pressed)
 * via TextChangedListener object listeners
 */
public class TextField extends JTextField {

    public interface TextChangedListener {

        void textChanged(String newText);
    }

    private static final String DEFAULT_VALUE = "";

    private String currentValue;

    private Set<TextChangedListener> textChangedListeners = new HashSet<>();

    public TextField() {
        this(DEFAULT_VALUE, 20);
    }

    public TextField(String text) {
        this(text, 20);
    }

    public TextField(int columns) {
        this(DEFAULT_VALUE, columns);
    }

    public TextField(String text, int columns) {
        super(text, columns);
        setMargin(new Insets(2, 6, 2, 6));
        new DefaultContextMenu().add(this);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                registerNewValue(getText());
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // "Enter" key
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerNewValue(getText());
                }
            }
        });
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        registerNewValue(t);
    }

    public void addTextChangeListener(TextChangedListener listener) {
        this.textChangedListeners.add(listener);
    }

    public void removeTextChangeListener(TextChangedListener listener) {
        this.textChangedListeners.remove(listener);
    }

    private void registerNewValue(String newValue) {
        if (newValue == null) {
            newValue = "";
        }

        if (!newValue.equals(this.currentValue)) {
            this.currentValue = newValue;
            if (this.textChangedListeners != null && this.textChangedListeners.size() > 0) {
                this.textChangedListeners.forEach(l -> l.textChanged(this.currentValue));
            }
        }
    }
}
