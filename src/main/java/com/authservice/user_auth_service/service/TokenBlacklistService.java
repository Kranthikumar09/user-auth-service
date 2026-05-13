package com.authservice.user_auth_service.service;

import com.authservice.user_auth_service.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
@AllArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;
    private final MetricsService metricsService;

    public void blacklistToken(String token) {
        String key = "blacklist:" + token;
        long expirationTime = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();

        if(expirationTime > 0) {
        redisTemplate.opsForValue().set(key, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);
        metricsService.incrementBlacklistedToken();
        }
    }

    public boolean isBlacklisted(String token) {
        String key = "blacklist:" + token;
        return redisTemplate.hasKey(key);
    }

}
