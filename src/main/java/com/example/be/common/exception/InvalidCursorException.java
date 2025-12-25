package com.example.be.concert.exception;

import com.example.be.common.exception.BusinessException;

public class InvalidCursorException extends BusinessException {

  public InvalidCursorException(String message) {
    super(message);
  }
}
