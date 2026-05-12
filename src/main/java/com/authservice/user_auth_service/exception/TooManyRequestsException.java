package com.authservice.user_auth_service.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = org.springframework.http.HttpStatus.TOO_MANY_REQUESTS, reason = "Too many login attempts. Please try again later.")
public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
