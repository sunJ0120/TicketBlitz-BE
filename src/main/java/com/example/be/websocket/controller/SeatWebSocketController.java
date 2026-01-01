package com.example.be.websocket.controller;

import com.example.be.websocket.dto.SeatHoldRequest;
import com.example.be.websocket.dto.SeatReleaseRequest;
import com.example.be.websocket.dto.SeatResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeatWebSocketController {

  @MessageMapping("/seats/hold")
  @SendToUser("/queue/seat-result")
  public SeatResponse holdSeat(SeatHoldRequest request, Principal principal) {
    Long userId = Long.parseLong(principal.getName());
    log.info("좌석 점유 요청 - userId = {}, seatId = {}", userId, request.seatId());

    // TODO : Redis를 활용한 좌석 점유 상태 관리하는 로직 필요
    return null;
  }

  @MessageMapping("/seats/release")
  @SendToUser("/queue/seat-result")
  public SeatResponse releaseSeat(SeatReleaseRequest request, Principal principal) {
    Long userId = Long.parseLong(principal.getName());
    log.info("좌석 해제 요청 - userId = {}, seatId = {}", userId, request.seatId());

    // TODO : Redis를 활용한 좌석 점유 해제 상태 관리하는 로직 필요
    return null;
  }
}
