package com.example.be.websocket.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatErrorCode {
  // 점유 실패
  SEAT_ALREADY_OCCUPIED("SEAT_001", "다른 사용자가 선택 중인 좌석입니다"),
  SEAT_ALREADY_SOLD("SEAT_002", "이미 판매 완료된 좌석입니다"),
  MAX_SEATS_EXCEEDED("SEAT_003", "최대 4석까지만 선택 가능합니다"),

  // 해제 실패
  RELEASE_FAILED("SEAT_004", "본인이 점유한 좌석이 아닙니다"),
  HOLD_EXPIRED("SEAT_005", "점유 시간이 만료되었습니다"),

  // 인증/권한
  UNAUTHORIZED("SEAT_006", "인증이 필요합니다"),
  NOT_IN_QUEUE("SEAT_007", "대기열을 통과하지 않았습니다");

  private final String code;
  private final String message;
}
