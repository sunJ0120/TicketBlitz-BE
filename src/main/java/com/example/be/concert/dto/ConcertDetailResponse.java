package com.example.be.concert.dto;

import com.example.be.concert.domain.Concert;
import java.time.LocalDateTime;
import java.util.List;

public record ConcertDetailResponse(
    Long id,
    String title,
    String artist,
    String description,
    String posterUrl,
    String genre,
    String genreDisplayName,
    String status,
    String statusDisplayName,
    String buildingName,
    String hallName,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime bookingStartAt,
    LocalDateTime bookingEndAt,
    List<SectionPriceDto> sections) {

  public static ConcertDetailResponse from(Concert concert) {
    // @formatter:off
    return new ConcertDetailResponse(
        concert.getId(),
        concert.getTitle(),
        concert.getArtist(),
        concert.getDescription(),
        concert.getPosterUrl(),
        concert.getGenre().name(),
        concert.getGenre().getDisplayName(),
        concert.getConcertStatus().name(),
        concert.getConcertStatus().getDisplayName(),
        concert.getHallTemplate().getBuilding().getName(),
        concert.getHallTemplate().getHallName(),
        concert.getStartDate(),
        concert.getEndDate(),
        concert.getBookingStartAt(),
        concert.getBookingEndAt(),
        concert.getConcertSections().stream().map(SectionPriceDto::from).toList());
  }
}
