package com.example.be.concert.enums;

import static com.example.be.concert.domain.QConcert.concert;

import com.example.be.common.exception.InvalidCursorException;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcertSortType {
  VIEW_COUNT("viewCount", concert.viewCount),
  PRICE("price", concert.minPrice),
  TICKET_OPEN_AT("ticketOpenAt", concert.bookingStartAt),
  CONCERT_DATE("concertDate", concert.startDate);

  private final String value;
  private final ComparableExpressionBase<?> field;

  public static ConcertSortType from(String value) {
    return Arrays.stream(values())
        .filter(type -> type.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new InvalidCursorException(value));
  }
}
