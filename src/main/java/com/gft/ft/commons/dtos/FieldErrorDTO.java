package com.gft.ft.commons.dtos;

/**
 * Created on 14/11/16.
 */
public class FieldErrorDTO {
    private String field;

    private String message;

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public FieldErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
