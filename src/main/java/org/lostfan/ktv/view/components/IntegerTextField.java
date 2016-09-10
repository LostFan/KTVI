package org.lostfan.ktv.view.components;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IntegerTextField extends TextField {

    private class IntegerDocumentFilter extends DocumentFilter {

        private boolean hasSign;

        private boolean hasPoint;

        private boolean willBeInteger(String insertion, int offset, int lengthToDelete) {
            String text = getText();
//            hasPoint = text.contains(".");
//            // The string has already a sign
//            // Inserting in the beginning
            boolean _hasSign;
//            boolean _hasPoint = hasPoint;
//
//            // Insertion at the beginning
//            if (offset == 0) {
//                if (hasSign && lengthToDelete == 0) {
//                    // There is a sign in the very beginning
//                    // Nothing is allowed to be placed before it
//                    return false;
//                }
//                else {
//                    // Sign at the beginning, then digits only
//                    if (!insertion.matches("^[-\\+]?\\d*(\\d[\\.])?\\d{0,2}$")) {
//                        return false;
//                    }
//                    if (insertion.charAt(0) == '+' || insertion.charAt(0) == '-') {
//                        _hasSign = true;
//                    } else {
//                        _hasSign = false;
//                    }
//                }
//            } else if (!insertion.matches("^\\d+$")
//                    && !insertion.matches("^\\d?([\\.])?\\d{0,2}$")) {
//                return false;
//            }
//            if (hasPoint && insertion.contains(".")) {
//                if (lengthToDelete > 0) {
//                    if (!text.substring(offset, offset + lengthToDelete).contains(".")) {
//                        return false;
//                    }
//                } else {
//                    return false;
//                }
//            }
//
//
//            if(insertion.contains(".")) {
//                _hasPoint = true;
//            }
//
//
//            if (text.length() + insertion.length() - lengthToDelete > (_hasSign ? 10 : 9)) {
//                return false;
//            }



                String newText = text.substring(0, offset) + insertion;
                if(text.length() != offset) {
                    newText += text.substring(offset + lengthToDelete, text.length());
                }

            if (newText.charAt(0) == '+' || newText.charAt(0) == '-') {
                _hasSign = true;
            } else {
                _hasSign = false;
            }

            if (!newText.matches("^[-\\+]?\\d*(\\d[\\.])?\\d{0,2}$")) {
                return false;
            }
            int lengthTextAfterPoint = 0;
            if(newText.contains(".")) {
                int pointPosition = newText.indexOf(".");
                lengthTextAfterPoint = newText.substring(pointPosition).length();
            }
            if (newText.length() - lengthTextAfterPoint > (_hasSign ? 10 : 9)) {
                return false;
            }

//
//            hasSign = _hasSign;
//            hasPoint = _hasPoint;
            return true;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (willBeInteger(string, offset, 0)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (willBeInteger(text, offset, length)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

            if (offset == 0) {
                hasSign = false;
                hasPoint = false;
            }
            super.remove(fb, offset, length);
        }
    }

    public IntegerTextField() {
        super(20);
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
        if(getText().equals("-") || getText().equals("+")) {
            return null;
        }
        Double aDouble = Double.parseDouble(getText());
        System.out.println(aDouble.intValue());
        return aDouble.intValue();
    }

    public void setValue(Integer value) {
        if (value == null) {
            setText("");
        } else {
            setText(String.valueOf(value));
        }
    }
}
