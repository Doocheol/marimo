package com.ssafy.marimo.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String responseMessage) {
        super(HttpStatus.BAD_REQUEST, responseMessage);
    }
}
