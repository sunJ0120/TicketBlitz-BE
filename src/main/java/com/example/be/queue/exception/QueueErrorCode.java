package com.example.be.queue.exception;

import com.example.be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QueueErrorCode implements ErrorCode {
  // 404
  CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "QUEUE_001", "존재하지 않는 공연입니다"),

  // 409
  ALREADY_IN_QUEUE(HttpStatus.CONFLICT, "QUEUE_002", "이미 대기열에 등록되어 있습니다"),
  ALREADY_ACTIVE(HttpStatus.CONFLICT, "QUEUE_003", "이미 입장 완료 상태입니다"),

  // 400
  QUEUE_NOT_OPEN(HttpStatus.BAD_REQUEST, "QUEUE_004", "대기열이 열려있지 않습니다"),

  // 500
  QUEUE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "QUEUE_005", "대기열 처리 중 오류가 발생했습니다");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
