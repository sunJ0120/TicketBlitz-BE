package com.example.be.concert.repository;

import static org.assertj.core.api.Assertions.*;

import com.example.be.concert.domain.Building;
import com.example.be.concert.domain.Concert;
import com.example.be.concert.domain.HallTemplate;
import com.example.be.concert.dto.ConcertSearchCondition;
import com.example.be.concert.enums.ConcertSortType;
import com.example.be.concert.enums.ConcertStatus;
import com.example.be.concert.enums.Genre;
import com.example.be.config.QueryDslConfig;
import com.example.be.fixture.ConcertFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
class ConcertRepositoryTest {

  @Autowired private ConcertRepository concertRepository;
  @Autowired private BuildingRepository buildingRepository;
  @Autowired private HallTemplateRepository hallTemplateRepository;

  private HallTemplate hall;

  @BeforeEach
  void setup() {
    Building building = buildingRepository.save(ConcertFixture.createBuilding());
    hall = hallTemplateRepository.save(ConcertFixture.createHallTemplate(building));
  }

  @Nested // test별 묶기
  @DisplayName("공연 상태별 카운트")
  class CountByStatus {

    @Test
    void 예매오픈_상태_공연_카운트() {
      // given
      concertRepository.save(
          ConcertFixture.createConcert(
              hall, "IU 콘서트", "IU", Genre.KPOP, ConcertStatus.BOOKING_OPEN));
      concertRepository.save(
          ConcertFixture.createConcert(
              hall, "BTS 콘서트", "BTS", Genre.KPOP, ConcertStatus.SCHEDULED));
      concertRepository.save(
          ConcertFixture.createConcert(
              hall, "종료된 공연", "Artist", Genre.SPORTS, ConcertStatus.BOOKING_CLOSED));

      // when
      long count = concertRepository.countByConcertStatus(ConcertStatus.BOOKING_OPEN);

      // then
      assertThat(count).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("공연 상세 조회")
  class FindByWithDetail {

    @Test
    void 공연_상세_조회_성공() {
      // given
      Concert saved =
          concertRepository.save(
              ConcertFixture.createConcertWithSections(
                  hall, "IU Concert", "IU", Genre.KPOP, ConcertStatus.BOOKING_OPEN));

      // when
      Optional<Concert> concert = concertRepository.findByIdWithDetail(saved.getId());

      // then
      assertThat(concert).isPresent();
      assertThat(concert.get().getConcertStatus()).isEqualTo(ConcertStatus.BOOKING_OPEN);
      assertThat(concert.get().getTitle()).isEqualTo("IU Concert");
    }

    @Test
    void 존재하지_않는_공연_조회시_빈값_반환() {
      // when
      Optional<Concert> concert = concertRepository.findByIdWithDetail(9999L);

      // then
      assertThat(concert).isEmpty();
    }

    @Test
    void fetch_join으로_연관_엔티티_한번에_로딩() {
      // given
      Concert saved =
          concertRepository.save(
              ConcertFixture.createConcertWithSections(
                  hall, "IU Concert", "IU", Genre.KPOP, ConcertStatus.BOOKING_OPEN));

      // when
      Optional<Concert> concert = concertRepository.findByIdWithDetail(saved.getId());

      // then
      assertThat(concert.get().getHallTemplate().getHallName()).isEqualTo(hall.getHallName());
      assertThat(concert.get().getHallTemplate().getBuilding().getName())
          .isEqualTo(hall.getBuilding().getName());
      assertThat(concert.get().getConcertSections()).hasSize(2);
    }

    @Test
    void 섹션_없는_공연도_조회_가능() {
      // given
      Concert saved =
          concertRepository.save(
              ConcertFixture.createConcert(
                  hall, "섹션없는 공연", "Artist", Genre.CLASSIC, ConcertStatus.BOOKING_CLOSED));

      // when
      Optional<Concert> concert = concertRepository.findByIdWithDetail(saved.getId());

      // then
      assertThat(concert.get().getHallTemplate().getHallName()).isEqualTo(hall.getHallName());
      assertThat(concert.get().getHallTemplate().getBuilding().getName())
          .isEqualTo(hall.getBuilding().getName());
    }
  }

  @Nested
  @DisplayName("커서 페이지네이션")
  class CursorPagination {

    @Test
    void 첫_페이지_조회() {
      // given
      List<Concert> concerts = ConcertFixture.createConcerts(hall, 50); // 콘서트 50개 생성
      concertRepository.saveAll(concerts);

      ConcertSearchCondition condition =
          new ConcertSearchCondition(
              null, null, null, null, null, null, null); // 페이지네이션 test를 위한 것이므로, 검색 조건 없음

      // when
      List<Concert> result =
          concertRepository.findWithCursor(
              condition,
              null,
              20,
              ConcertSortType.CONCERT_DATE,
              true); // 20개씩, 시작 시간으로 정렬해서 cursor는 첫 페이지라 null

      // then
      assertThat(result).hasSize(21); // size + 1 (hasNext 판단용)
    }

    @Test
    void 장르_필터_적용() {
      // given
      concertRepository.save(
          ConcertFixture.createConcert(
              hall, "IU 콘서트", "IU", Genre.KPOP, ConcertStatus.BOOKING_OPEN));
      concertRepository.save(
          ConcertFixture.createConcert(
              hall, "클래식 공연", "조성진", Genre.CLASSIC, ConcertStatus.BOOKING_OPEN));
      concertRepository.save(
          ConcertFixture.createConcert(
              hall, "베이비몬스터 콘서트", "베이비몬스터", Genre.KPOP, ConcertStatus.SCHEDULED));

      ConcertSearchCondition condition =
          new ConcertSearchCondition(null, Genre.KPOP, null, null, null, null, null);

      // when
      List<Concert> result =
          concertRepository.findWithCursor(condition, null, 20, ConcertSortType.CONCERT_DATE, true);

      // then
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getGenre()).isEqualTo(Genre.KPOP);
    }

    @Test
    void 가격_범위_필터_적용() {
      // given
      concertRepository.save(ConcertFixture.createConcertWithPrice(hall, "저가 공연", 50000, 80000));
      concertRepository.save(ConcertFixture.createConcertWithPrice(hall, "고가 공연", 150000, 300000));

      ConcertSearchCondition condition =
          new ConcertSearchCondition(null, null, null, null, null, 80000, 150000); // 끝 부분에 걸리게끔 설정
      // when
      List<Concert> result =
          concertRepository.findWithCursor(condition, null, 20, ConcertSortType.PRICE, true);

      // then
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getTitle()).isEqualTo("저가 공연");
    }

    @Test
    void 날짜_범위_필터_적용() {
      // given
      LocalDateTime jan15 = LocalDateTime.of(2025, 1, 15, 19, 0);
      LocalDateTime jan20 = LocalDateTime.of(2025, 1, 20, 19, 0);
      LocalDateTime feb10 = LocalDateTime.of(2025, 2, 10, 19, 0);

      concertRepository.save(
          ConcertFixture.createConcertWithDate(hall, "1월 공연", jan15, jan15.plusHours(3)));
      concertRepository.save(
          ConcertFixture.createConcertWithDate(hall, "내 생일에 하는 공연", jan20, jan20.plusHours(3)));
      concertRepository.save(
          ConcertFixture.createConcertWithDate(hall, "2월 공연", feb10, feb10.plusHours(3)));

      LocalDate searchStart = LocalDate.of(2025, 1, 15); // 1월 공연들만 걸려야 한다.
      LocalDate searchEnd = LocalDate.of(2025, 1, 20);

      ConcertSearchCondition condition =
          new ConcertSearchCondition(null, null, null, searchStart, searchEnd, null, null);

      // when
      List<Concert> result =
          concertRepository.findWithCursor(
              condition, null, 20, ConcertSortType.CONCERT_DATE, false);

      // then
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getTitle()).isEqualTo("내 생일에 하는 공연");
    }
  }
}
