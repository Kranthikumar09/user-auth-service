package com.authservice.user_auth_service.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final Counter registrationCounter;
    private final Counter logoutCounter;
    private final Counter rateLimitCounter;
    private final Counter blacklistedTokenCounter;

    public MetricsService(MeterRegistry registry) {
        this.loginSuccessCounter = Counter.builder("auth.login.success")
                .description("Total successful logins")
                .register(registry);
        this.loginFailureCounter = Counter.builder("auth.login.failure")
                .description("Total failed login attempts")
                .register(registry);
        this.registrationCounter = Counter.builder("auth.registration")
                .description("Total user registrations")
                .register(registry);
        this.logoutCounter = Counter.builder("auth.logout")
                .description("Total logouts")
                .register(registry);
        this.rateLimitCounter = Counter.builder("auth.rate.limited")
                .description("Total rate limited requests")
                .register(registry);
        this.blacklistedTokenCounter = Counter.builder("auth.token.blacklisted")
                .description("Total blacklisted tokens")
                .register(registry);
    }

    public void incrementLoginSuccess() { loginSuccessCounter.increment(); }
    public void incrementLoginFailure() { loginFailureCounter.increment(); }
    public void incrementRegistration() { registrationCounter.increment(); }
    public void incrementLogout() { logoutCounter.increment(); }
    public void incrementRateLimited() { rateLimitCounter.increment(); }
    public void incrementBlacklistedToken() { blacklistedTokenCounter.increment(); }
}