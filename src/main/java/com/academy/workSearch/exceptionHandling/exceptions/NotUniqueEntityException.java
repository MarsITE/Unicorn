package com.academy.workSearch.exceptionHandling.exceptions;

public class NotUniqueEntityException extends RuntimeException {
    public NotUniqueEntityException(String message) {
        super(message);
    }

}
