package com.LogHub.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ProblemDetail handleAppNotFound(ApplicationNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Application Not Found");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ProblemDetail handleEmailDuplicate(EmailAlreadyRegisteredException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Email Already Registered");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ProblemDetail handleInvalidApiKey(InvalidApiKeyException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Invalid API Key");
        problem.setDetail(ex.getMessage());
        return problem;
    }
}
