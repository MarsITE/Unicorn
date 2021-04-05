package com.academy.workSearch.exceptionHandling;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorMessage {
    private final HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private final String message;
    private final String description;

    public ErrorMessage(HttpStatus status, String message, String description){
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.description = description;
    }

    public HttpStatus getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public String getDescription(){
        return description;
    }
}
