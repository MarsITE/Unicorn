package com.academy.workSearch.exceptionHandling.exceptions;

public class NoSuchEntityException extends RuntimeException{
    public NoSuchEntityException(String message){
        super(message);
    }
}
