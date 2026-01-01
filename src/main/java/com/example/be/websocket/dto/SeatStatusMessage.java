package com.example.be.websocket.dto;

import java.time.LocalDateTime;

// 브로드캐스트 (/topic/performance/{id}/seats)
public record SeatStatusMessage(
    String seatId,
    String status, // "AVAILABLE", "OCCUPIED", "SOLD"
    Long holderId,
    LocalDateTime timestamp) {}
