package com.example.be.concert.repository;

import static org.assertj.core.api.Assertions.*;

import com.example.be.concert.domain.Building;
import com.example.be.concert.domain.Concert;
import com.example.be.concert.domain.HallTemplate;
import com.example.be.concert.enums.ConcertStatus;
import com.example.be.concert.enums.Genre;
import com.example.be.config.QueryDslConfig;
import com.example.be.fixture.ConcertFixture;
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
