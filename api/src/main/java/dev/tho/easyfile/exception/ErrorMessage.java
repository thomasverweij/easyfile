package dev.tho.easyfile.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ErrorMessage {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ErrorMessage(HttpStatus status, List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public ErrorMessage(HttpStatus status, String error) {
        super();
        this.status = status;
        errors = Arrays.asList(error);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}