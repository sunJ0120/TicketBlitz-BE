package com.example.be.queue.exception;

import static com.example.be.common.exception.GlobalErrorCode.INVALID_CURSOR;

import com.example.be.common.exception.BusinessException;
import com.example.be.common.exception.GlobalErrorCode;

public class AlreadyActiveException extends BusinessException {

  public AlreadyActiveException() {
    super(QueueErrorCode.ALREADY_ACTIVE);
  }
}