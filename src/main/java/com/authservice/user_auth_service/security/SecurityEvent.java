package com.authservice.user_auth_service.security;

public enum SecurityEvent {
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT,
    REGISTRATION,
    TOKEN_BLACKLISTED,
    RATE_LIMITED,
    UNAUTHORIZED_ACCESS,
    TOKEN_INVALID
}
