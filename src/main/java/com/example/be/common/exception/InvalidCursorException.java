package com.example.be.common.exception;

import static com.example.be.common.exception.GlobalErrorCode.INVALID_CURSOR;

public class InvalidCursorException extends BusinessException {

  public InvalidCursorException() {
    super(INVALID_CURSOR);
  }

  public InvalidCursorException(String value) {
    super(GlobalErrorCode.INVALID_CURSOR, "잘못된 커서 값입니다: " + value);
  }
}
