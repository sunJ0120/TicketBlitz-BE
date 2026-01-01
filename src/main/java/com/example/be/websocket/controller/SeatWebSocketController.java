package com.example.be.websocket.controller;

import com.example.be.websocket.dto.SeatHoldRequest;
import com.example.be.websocket.dto.SeatReleaseRequest;
import com.example.be.websocket.dto.SeatResponse;
import com.example.be.websocket.service.SeatHoldService;
import java.security.Principal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeatWebSocketController {

  private final SeatHoldService seatHoldService;

  @MessageMapping("/seats/hold")
  @SendToUser("/queue/seat-result")
  public SeatResponse holdSeat(SeatHoldRequest request, Principal principal) {
    Long userId = Long.parseLong(principal.getName());
    log.info("좌석 점유 요청 - userId = {}, seatId = {}", userId, request.seatId());

    boolean success = seatHoldService.holdSeat(request.performanceId(), request.seatId(), userId);

    if (success) {
      LocalDateTime expiresAt =
          seatHoldService.getExpirationTime(request.performanceId(), request.seatId());

      return new SeatResponse("SUCCESS", "HOLD", request.seatId(), null, null, expiresAt);
    } else {
      return new SeatResponse(
          "ERROR", "HOLD", request.seatId(), "SEAT_ALREADY_OCCUPIED", "다른 사용자가 선택 중인 좌석입니다", null);
    }
  }

  @MessageMapping("/seats/release")
  @SendToUser("/queue/seat-result")
  public SeatResponse releaseSeat(SeatReleaseRequest request, Principal principal) {
    Long userId = Long.parseLong(principal.getName());
    log.info("좌석 해제 요청 - userId = {}, seatId = {}", userId, request.seatId());

    boolean success =
        seatHoldService.releaseSeat(request.performanceId(), request.seatId(), userId);

    if (success) {
      return new SeatResponse("SUCCESS", "RELEASE", request.seatId(), null, null, null);
    } else {
      return new SeatResponse(
          "ERROR", "RELEASE", request.seatId(), "RELEASE_FAILED", "본인이 점유한 좌석이 아닙니다", null);
    }
  }
}
