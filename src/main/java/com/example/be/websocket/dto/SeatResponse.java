package com.example.be.websocket.dto;

import java.time.LocalDateTime;

// 개인 응답 (/user/queue/seat-result)
public record SeatResponse(
    String type,
    String action,
    String seatId,
    String code,
    String message,
    LocalDateTime expiresAt // 성공 시
    ) {}
