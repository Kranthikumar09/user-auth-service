package com.authservice.user_auth_service.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RateLimitService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int WINDOW_SECONDS = 60;
    private final StringRedisTemplate redisTemplate;

    public boolean isRateLimited(String ipAddress) {
        String key = "rate_limit:" + ipAddress;
        String attemptStr = redisTemplate.opsForValue().get(key);
        if(attemptStr == null) {
            return false;
        } else {
            int attempts = Integer.parseInt(attemptStr);
            return attempts >= MAX_ATTEMPTS;
        }
    }

    public void recordFailedAttempt(String ipAddress){
        String key = "rate_limit:" + ipAddress;
        Long incremented = redisTemplate.opsForValue().increment(key);
        if(incremented != null && incremented == 1L) {
            //only on first failed attempt, set the expiration time
            redisTemplate.expire(key, WINDOW_SECONDS, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    public void clearAttempts(String ipAddress) {
        String key = "rate_limit:" + ipAddress;
        redisTemplate.delete(key);
    }
}
