package com.example.be.websocket.service;

import com.example.be.websocket.util.SeatRedisKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatHoldService {
  private static final long HOLD_TTL_SECONDS = 300; // 5분

  private final StringRedisTemplate redisTemplate;

  /** 좌석 점유 */
  public boolean holdSeat(Long performanceId, String seatId, Long userId) {
    String key = SeatRedisKey.hold(performanceId, seatId);
    Boolean success =
        redisTemplate
            .opsForValue()
            .setIfAbsent(key, String.valueOf(userId), Duration.ofSeconds(HOLD_TTL_SECONDS));

    return Boolean.TRUE.equals(success);
  }

  /** 좌석 점유 해제 */
  public boolean releaseSeat(Long performanceId, String seatId, Long userId) {
    String key = SeatRedisKey.hold(performanceId, seatId);
    String holderId = redisTemplate.opsForValue().get(key); // 본인이 점유한 좌석만 해제가 가능하기 때문이다.

    if (holderId != null && holderId.equals(String.valueOf(userId))) {
      redisTemplate.delete(key);
      return true;
    }
    return false;
  }

  /** 좌석 점유자 조회 */
  public Long getHolder(Long performanceId, String seatId) {
    String key = SeatRedisKey.hold(performanceId, seatId);
    String holderId = redisTemplate.opsForValue().get(key);

    return holderId == null ? null : Long.parseLong(holderId);
  }

  /** 점유 만료 시간 조회 */
  public LocalDateTime getExpirationTime(Long performanceId, String seatId) {
    String key = SeatRedisKey.hold(performanceId, seatId);
    Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

    if (ttl != null && ttl > 0) {
      return LocalDateTime.now().plusSeconds(ttl);
    }
    return null;
  }
}
