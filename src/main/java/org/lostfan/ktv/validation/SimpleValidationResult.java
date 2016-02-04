package org.lostfan.ktv.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimpleValidationResult implements ValidationResult {

    private class GeneralError implements Error {

        private String message;
        private String field;
        private Object[] params;

        public GeneralError(String message, String field, Object[] params) {
            this.message = message;
            this.field = field;
            if (params == null) {
                this.params = new Object[0];
            } else {
                this.params = params;
            }
        }

        @Override
        public String getMessage() {
            return this.message;
        }

        @Override
        public String getField() {
            return this.field;
        }

        @Override
        public Object[] getParams() {
            return this.params;
        }
    }

    private List<Error> errors;
    private List<Error> errorsView;

    public SimpleValidationResult() {
        this.errors = new ArrayList<>();
        this.errorsView = Collections.unmodifiableList(this.errors);
    }

    @Override
    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

    @Override
    public void addError(String message) {
        addError(new GeneralError(message, null, null));
    }

    @Override
    public void addError(String message, Object... params) {
        addError(new GeneralError(message, null, params));
    }

    @Override
    public void addError(String message, String field) {
        addError(new GeneralError(message, field, null));
    }

    @Override
    public void addError(String message, String field, Object... params) {
        addError(new GeneralError(message, field, params));
    }

    @Override
    public List<Error> getErrors() {
        return this.errorsView;
    }

    private void addError(Error error) {
        this.errors.add(error);
    }

    @Override
    public Iterator<Error> iterator() {
        return this.errors.iterator();
    }
}
