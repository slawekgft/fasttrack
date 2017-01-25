package com.gft.ft.commons.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 14/11/16.
 */
public class ValidationErrorInfoDTO {

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public ValidationErrorInfoDTO() {

    }

    public void addFieldError(String path, String message) {
        FieldErrorDTO error = new FieldErrorDTO(path, message);
        fieldErrors.add(error);
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}
