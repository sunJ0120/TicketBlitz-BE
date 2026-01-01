package com.example.be.websocket.controller;

import com.example.be.websocket.dto.SeatHoldRequest;
import com.example.be.websocket.dto.SeatReleaseRequest;
import com.example.be.websocket.dto.SeatResponse;
import com.example.be.websocket.enums.SeatAction;
import com.example.be.websocket.exception.SeatErrorCode;
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

      return SeatResponse.success(SeatAction.HOLD, request.seatId(), expiresAt);
    } else {
      return SeatResponse.error(
          SeatAction.HOLD, request.seatId(), SeatErrorCode.SEAT_ALREADY_OCCUPIED);
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
      return SeatResponse.success(SeatAction.RELEASE, request.seatId(), null);
    } else {
      return SeatResponse.error(SeatAction.RELEASE, request.seatId(), SeatErrorCode.RELEASE_FAILED);
    }
  }
}
