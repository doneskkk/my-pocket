package com.donesk.moneytracker.exception.config;

import com.donesk.moneytracker.exception.BudgetNotFoundException;
import com.donesk.moneytracker.exception.CategoryNotFoundException;
import com.donesk.moneytracker.exception.TransactionNotFoundException;
import com.donesk.moneytracker.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CategoryNotFoundException.class})
    public ResponseEntity<AppError> handleCategoryNotFoundException(CategoryNotFoundException ex){
        AppError exceptionConfig =
                new AppError(NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<>(exceptionConfig, NOT_FOUND);
    }

    @ExceptionHandler(value = {TransactionNotFoundException.class})
    public ResponseEntity<AppError> handleTransactionNotFoundException(TransactionNotFoundException ex){
        AppError exceptionConfig =
                new AppError(NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<>(exceptionConfig, NOT_FOUND);
    }

    @ExceptionHandler(value = {BudgetNotFoundException.class})
    public ResponseEntity<AppError> handleBudgetNotFoundException(BudgetNotFoundException ex){
        AppError exceptionConfig =
                new AppError(NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<>(exceptionConfig, NOT_FOUND);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<AppError> handleUserNotFoundException(UserNotFoundException ex){
        AppError exceptionConfig =
                new AppError(NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<>(exceptionConfig, NOT_FOUND);
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach( (error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

}
