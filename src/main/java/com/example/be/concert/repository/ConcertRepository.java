package com.example.be.concert.repository;

import com.example.be.concert.domain.Concert;
import com.example.be.concert.enums.ConcertStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

  long countByConcertStatus(ConcertStatus status);    // 상태별 카운트

  List<Concert> findTop10ByConcertStatusInOrderByBookingStartAtDesc(
      List<ConcertStatus> statuses); // 상태 목록으로 조회 + 정렬 + 개수 제한
}
