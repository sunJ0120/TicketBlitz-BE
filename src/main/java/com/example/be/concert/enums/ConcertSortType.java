package com.example.be.concert.enums;

import com.example.be.common.exception.InvalidCursorException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcertSortType {
  VIEW_COUNT("viewCount"),
  PRICE("price"),
  TICKET_OPEN_AT("ticketOpenAt"),
  CONCERT_DATE("concertDate");

  private final String value;

  public static ConcertSortType from(String value) {
    return Arrays.stream(values())
        .filter(type -> type.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new InvalidCursorException(value));
  }
}
