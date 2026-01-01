package com.example.be.websocket.dto;

import com.example.be.concert.enums.SeatStatus;
import java.time.LocalDateTime;

// 브로드캐스트 (/topic/performance/{id}/seats)
public record SeatStatusMessage(
    String seatId, String status, Long holderId, LocalDateTime timestamp) {

  public static SeatStatusMessage of(String seatId, SeatStatus status, Long holderId) {
    return new SeatStatusMessage(seatId, status.name(), holderId, LocalDateTime.now());
  }
}
