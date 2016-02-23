package org.lostfan.ktv;

public class ApplicationException extends RuntimeException {

    private String code;

    public ApplicationException(String code, Throwable cause) {
        super(code, cause);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
