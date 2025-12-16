package com.example.be.auth.service;

import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

  private final RedisTemplate<String, Object> redisTemplate;
  private static final String REFRESH_TOKEN_PREFIX = "refresh:";

  public String generateKey(Long userId) {
    return REFRESH_TOKEN_PREFIX + userId;
  }

  public void addToWhitelist(Long userId, String refreshToken, long expirationMillis) {
    String key = generateKey(userId);

    redisTemplate.opsForValue()
        .set(
            key,
            refreshToken,
            expirationMillis,
            TimeUnit.MILLISECONDS
        );
  }


}
