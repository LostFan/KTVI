package org.lostfan.ktv.validation;

public interface Error {

    String getMessage();

    String getField();

    Object[] getParams();
}
