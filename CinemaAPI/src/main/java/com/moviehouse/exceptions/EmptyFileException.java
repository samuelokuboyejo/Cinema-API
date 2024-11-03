package com.moviehouse.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmptyFileException extends Throwable {
    public EmptyFileException(String message) {
        super(message);
    }
}
