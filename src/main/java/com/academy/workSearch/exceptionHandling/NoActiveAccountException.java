package com.academy.workSearch.exceptionHandling;

import org.springframework.security.core.AuthenticationException;

public class NoActiveAccountException extends AuthenticationException {
    public NoActiveAccountException(String message) {
        super(message);
    }
}
