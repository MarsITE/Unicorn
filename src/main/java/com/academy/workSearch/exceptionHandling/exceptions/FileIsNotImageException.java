package com.academy.workSearch.exceptionHandling.exceptions;

public class FileIsNotImageException extends RuntimeException {
    public FileIsNotImageException(String message) {
        super(message);
    }
}
