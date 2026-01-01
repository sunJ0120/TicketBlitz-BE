package com.example.be.websocket.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatAction {
  HOLD("HOLD"),
  RELEASE("RELEASE");

  private final String value;
}
