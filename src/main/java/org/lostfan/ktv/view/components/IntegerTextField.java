package org.lostfan.ktv.view.components;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IntegerTextField extends JTextField {

    private class IntegerDocumentFilter extends DocumentFilter {

        private boolean hasSign;

        private boolean willBeInteger(String insertion, int offset) {
            String text = getText();
            // The string has already a sign
            // Inserting in the beginning
            boolean _hasSign = hasSign;
            if (hasSign && offset == 0) {
                // No more signs
                if (insertion.charAt(0) == '+' || insertion.charAt(0) == '-') {
                    return false;
                }
                // Digits only
                if (!insertion.matches("^\\d+$")) {
                    return false;
                }
            } else if (offset == 0 && !hasSign) {
                // Sign at the beginning, then digits only
                if (!insertion.matches("^[-\\+]?\\d+$")) {
                    return false;
                }
                if (insertion.charAt(0) == '+' || insertion.charAt(0) == '-') {
                    _hasSign = true;
                }
            } else if (!insertion.matches("^\\d+$")) {
                return false;
            }

            if (text.length() + insertion.length() > (_hasSign ? 10 : 9)) {
                return false;
            }

            hasSign = _hasSign;
            return true;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (willBeInteger(string, offset)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (willBeInteger(text, offset)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset == 0) {
                hasSign = false;
            }
            super.remove(fb, offset, length);
        }
    }

    public IntegerTextField() {
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
    }

    public IntegerTextField(Integer initialValue) {
        this();
        setValue(initialValue);

    }

    public Integer getValue() {
        if (getText().length() == 0) {
            return null;
        }
        return Integer.parseInt(getText());
    }

    public void setValue(Integer value) {
        if (value == null) {
            setText("");
        } else {
            setText(String.valueOf(value));
        }
    }
}
