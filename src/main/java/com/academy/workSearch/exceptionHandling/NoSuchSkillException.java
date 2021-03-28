package com.academy.workSearch.exceptionHandling;

public class NoSuchSkillException extends RuntimeException{
    public NoSuchSkillException(String message) {
        super(message);
    }
}
