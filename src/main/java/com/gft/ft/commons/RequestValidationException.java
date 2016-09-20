package com.gft.ft.commons;

/**
 * Created by e-srwn on 2016-09-20.
 */
public class RequestValidationException extends Exception {
    private String fieldName;

    public RequestValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
