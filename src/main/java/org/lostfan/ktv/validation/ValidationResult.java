package org.lostfan.ktv.validation;

import java.util.List;

public interface ValidationResult extends Iterable<Error> {

    boolean hasErrors();

    void addError(String message);

    void addError(String message, Object... params);

    void addError(String message, String field);

    void addError(String message, String field, Object... params);

    List<Error> getErrors();

    static ValidationResult createEmpty() {
        return new SimpleValidationResult();
    }
}
