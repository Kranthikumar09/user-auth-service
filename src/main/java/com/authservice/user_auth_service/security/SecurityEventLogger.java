package com.authservice.user_auth_service.security;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SecurityEventLogger {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("SECURITY_EVENTS");


    public void logEvent(SecurityEvent event, String username, String ipAddress, String details) {
        String logMessage = String.format("Event: %s, User: %s, IP: %s, Details: %s, timestamp=%s",
                event.name(), username, ipAddress, details, LocalDateTime.now());
        logger.info(logMessage);
    }

}
