package com.example.be.fixture;

import com.example.be.concert.domain.Building;
import com.example.be.concert.domain.Concert;
import com.example.be.concert.domain.ConcertSection;
import com.example.be.concert.domain.HallTemplate;
import com.example.be.concert.enums.ConcertStatus;
import com.example.be.concert.enums.Genre;
import com.example.be.concert.enums.SeatLabel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertFixture {

  public static Building createBuilding() {
    return Building.builder()
        .name("테스트 공연장")
        .address("서울시 강남구")
        .latitude(37.5)
        .longitude(127.0)
        .build();
  }

  public static HallTemplate createHallTemplate(Building building) {
    return HallTemplate.builder()
        .building(building)
        .hallName("테스트홀")
        .totalSeats(100)
        .totalRows(10)
        .build();
  }

  public static Concert createConcert(
      HallTemplate hall, String title, String artist, Genre genre, ConcertStatus status) {
    return Concert.builder()
        .hallTemplate(hall)
        .title(title)
        .artist(artist)
        .genre(genre)
        .concertStatus(status)
        .startDate(LocalDateTime.now().plusMonths(1))
        .endDate(LocalDateTime.now().plusMonths(1).plusHours(3))
        .bookingStartAt(LocalDateTime.now().minusDays(7))
        .bookingEndAt(LocalDateTime.now().plusMonths(1).minusHours(1))
        .viewCount(0L)
        .minPrice(100000)
        .maxPrice(200000)
        .build();
  }

  // 대량 생성
  public static List<Concert> createConcerts(HallTemplate hall, int count) {
    List<Concert> concerts = new ArrayList<>();
    String[] artists = {"IU", "BTS", "에스파", "세븐틴", "블랙핑크"};
    Genre[] genres = Genre.values();
    ConcertStatus[] statuses = {
      ConcertStatus.BOOKING_OPEN, ConcertStatus.SCHEDULED, ConcertStatus.SOLD_OUT
    };

    for (int i = 0; i < count; i++) {
      concerts.add(
          Concert.builder()
              .hallTemplate(hall)
              .title(artists[i % artists.length] + " 콘서트 " + i)
              .artist(artists[i % artists.length])
              .genre(genres[i % genres.length])
              .concertStatus(statuses[i % statuses.length])
              .startDate(LocalDateTime.now().plusDays(i))
              .endDate(LocalDateTime.now().plusDays(i).plusHours(3))
              .bookingStartAt(LocalDateTime.now().minusDays(7))
              .bookingEndAt(LocalDateTime.now().plusDays(i).minusHours(1))
              .viewCount((long) (Math.random() * 50000))
              .minPrice(50000 + (i * 10000) % 150000)
              .maxPrice(150000 + (i * 10000) % 100000)
              .build());
    }
    return concerts;
  }

  public static Concert createConcertWithSections(
      HallTemplate hall, String title, String artist, Genre genre, ConcertStatus status) {
    Concert concert = createConcert(hall, title, artist, genre, status);

    ConcertSection vip =
        ConcertSection.builder()
            .sectionLabel(SeatLabel.VIP)
            .rowStart(1)
            .rowEnd(5)
            .price(220000)
            .color("#FFD700")
            .build();

    ConcertSection r =
        ConcertSection.builder()
            .sectionLabel(SeatLabel.R)
            .rowStart(6)
            .rowEnd(10)
            .price(154000)
            .color("#FF69B4")
            .build();

    concert.getConcertSections().add(vip);
    concert.getConcertSections().add(r);

    return concert;
  }

  public static Concert createConcertWithPrice(
      HallTemplate hall, String title, int minPrice, int maxPrice) {
    return Concert.builder()
        .hallTemplate(hall)
        .title(title)
        .artist("Artist")
        .genre(Genre.KPOP)
        .concertStatus(ConcertStatus.BOOKING_OPEN)
        .startDate(LocalDateTime.now().plusMonths(1))
        .endDate(LocalDateTime.now().plusMonths(1).plusHours(3))
        .bookingStartAt(LocalDateTime.now().minusDays(7))
        .bookingEndAt(LocalDateTime.now().plusMonths(1).minusHours(1))
        .viewCount(0L)
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .build();
  }

  public static Concert createConcertWithDate(
      HallTemplate hall, String title, LocalDateTime startDate, LocalDateTime endDate) {
    return Concert.builder()
        .hallTemplate(hall)
        .title(title)
        .artist("Artist")
        .genre(Genre.KPOP)
        .concertStatus(ConcertStatus.BOOKING_OPEN)
        .startDate(startDate)
        .endDate(endDate)
        .bookingStartAt(LocalDateTime.now().minusDays(7))
        .bookingEndAt(LocalDateTime.now().plusMonths(1).minusHours(1))
        .viewCount(0L)
        .minPrice(100000)
        .maxPrice(200000)
        .build();
  }
}
