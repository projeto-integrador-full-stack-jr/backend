package com.mentoria.back_end_mentoria.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException(String msg) {
        super(msg);
    }
}