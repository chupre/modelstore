package com.byllameister.modelstore.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IpBruteForceProtectionService {

    private final Cache<@NonNull String, Integer> attemptsCache;

    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_TIME_MINUTES = 15;

    public IpBruteForceProtectionService() {
        this.attemptsCache = Caffeine.newBuilder()
                .expireAfterWrite(BLOCK_TIME_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    public void loginSucceeded(String ip) {
        attemptsCache.invalidate(ip);
    }

    public void loginFailed(String ip) {
        Integer attempts = attemptsCache.getIfPresent(ip);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(ip, attempts);
    }

    public boolean isBlocked(String ip) {
        Integer attempts = attemptsCache.getIfPresent(ip);
        return attempts != null && attempts >= MAX_ATTEMPTS;
    }
}
