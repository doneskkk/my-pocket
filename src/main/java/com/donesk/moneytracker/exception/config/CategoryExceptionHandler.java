package com.donesk.moneytracker.exception.config;

import com.donesk.moneytracker.exception.CategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(value = {CategoryNotFoundException.class})
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex){
        CategoryException categoryException =
                new CategoryException(ex.getMessage(), ex.getCause(), NOT_FOUND);

        return new ResponseEntity<>(categoryException, NOT_FOUND);
    }
}
