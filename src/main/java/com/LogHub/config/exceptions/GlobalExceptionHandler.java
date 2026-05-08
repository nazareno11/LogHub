package com.LogHub.config.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*Validaciones personalizadas */
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
    /*Excepciones de Validacion */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Error");
        problemDetail.setDetail("Error de validación en los campos enviados");

        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage));

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }
    
    /*Validaciones genericas*/
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {

        //Log del error para el backend usando Slf4j
        log.error("Error inesperado en la aplicación", ex);

        //Respuesta segura para el cliente (RFC 9457)
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Error interno del servidor");
        problem.setDetail("Ocurrió un error inesperado");
        problem.setProperty("errors", List.of("Contacte al administrador"));

        return problem;
    }
}
