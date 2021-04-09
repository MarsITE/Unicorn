package com.academy.workSearch.exceptionHandling;

import com.academy.workSearch.exceptionHandling.exceptions.AuthorizationException;
import com.academy.workSearch.exceptionHandling.exceptions.BadRequestException;
import com.academy.workSearch.exceptionHandling.exceptions.ForbiddenException;
import com.academy.workSearch.exceptionHandling.exceptions.NoActiveAccountException;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.exceptionHandling.exceptions.NoUniqueEntityException;
import com.academy.workSearch.exceptionHandling.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NoSuchEntityException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class, NoActiveAccountException.class})
    public ResponseEntity<ErrorMessage> handleAuthorizationException(AuthorizationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessage> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.METHOD_NOT_ALLOWED,
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoUniqueEntityException.class)
    public ResponseEntity<ErrorMessage> handleNoUniqueEntityException(NoUniqueEntityException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
