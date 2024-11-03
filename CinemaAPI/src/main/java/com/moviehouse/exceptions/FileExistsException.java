package com.moviehouse.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileExistsException extends RuntimeException{

    public FileExistsException(String message){
        super(message);
    }
}
