package com.academy.workSearch.exceptionHandling.exceptions;

public class NoUniqueEntityException extends RuntimeException {
    public NoUniqueEntityException(String message) {
        super(message);
    }

}
