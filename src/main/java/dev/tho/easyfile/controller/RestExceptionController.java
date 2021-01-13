package dev.tho.easyfile.controller;


import dev.tho.easyfile.exception.EntityNotFoundException;
import dev.tho.easyfile.exception.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NOT_FOUND, "Entity not found");
        return new ResponseEntity<Object>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST, "Bad request");
        return new ResponseEntity<Object>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }




//    @ExceptionHandler({ Exception.class })
//    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
//        ErrorMessage errorMessage = new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR, "error occurred");
//        return new ResponseEntity<Object>(
//                errorMessage, new HttpHeaders(), errorMessage.getStatus());
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorMessage errorMessage =
                new ErrorMessage(HttpStatus.BAD_REQUEST, errors);
        return handleExceptionInternal(
                ex, errorMessage, headers, errorMessage.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ErrorMessage errorMessage =
                new ErrorMessage(HttpStatus.BAD_REQUEST, error);
        return new ResponseEntity<Object>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }
}

