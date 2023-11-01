package com.controlefinanceiro.financas.handler;

import com.controlefinanceiro.financas.entities.error.ErrorMessage;
import com.controlefinanceiro.financas.entities.exceptions.ConflictException;
import com.controlefinanceiro.financas.entities.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {

        ErrorMessage message = new ErrorMessage("Not found.", HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handlerConflictException(ConflictException e) {

        ErrorMessage message = new ErrorMessage("Conflict.", HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}
