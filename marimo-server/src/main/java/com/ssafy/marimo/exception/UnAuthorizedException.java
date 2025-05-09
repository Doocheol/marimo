package com.ssafy.marimo.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BaseException{

    public UnAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }

    public UnAuthorizedException(String responseMessage) {
        super(HttpStatus.UNAUTHORIZED, responseMessage);
    }
}