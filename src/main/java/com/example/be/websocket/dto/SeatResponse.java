package com.example.be.websocket.dto;

import com.example.be.websocket.enums.SeatAction;
import com.example.be.websocket.exception.SeatErrorCode;
import java.time.LocalDateTime;

// 개인 응답 (/user/queue/seat-result)
public record SeatResponse(
    String type,
    String action,
    String seatId,
    String code,
    String message,
    LocalDateTime expiresAt // 성공 시
    ) {
  public static SeatResponse success(SeatAction action, String seatId, LocalDateTime expiresAt) {
    return new SeatResponse("SUCCESS", action.getValue(), seatId, null, null, expiresAt);
  }

  public static SeatResponse error(SeatAction action, String seatId, SeatErrorCode code) {
    return new SeatResponse(
        "ERROR", action.getValue(), seatId, code.getCode(), code.getMessage(), null);
  }
}
