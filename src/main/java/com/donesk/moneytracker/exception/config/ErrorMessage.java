package com.donesk.moneytracker.exception.config;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorMessage {
    public static String bindingResultHasErrors(BindingResult bd){
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errors = bd.getFieldErrors();
        for(FieldError error : errors) {
            errorMessage
                    .append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";\n");
        }
        return errorMessage.toString();
    }
}
