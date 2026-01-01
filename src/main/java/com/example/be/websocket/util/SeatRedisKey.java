package com.example.be.websocket.util;

public final class SeatRedisKey {

  private static final String SEAT_HOLD_PREFIX = "seat:hold:";

  private SeatRedisKey() {}

  public static String hold(Long performanceId, String seatId) {
    return SEAT_HOLD_PREFIX + performanceId + ":" + seatId;
  }
}
