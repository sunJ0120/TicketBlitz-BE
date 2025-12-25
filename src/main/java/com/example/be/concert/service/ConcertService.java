package com.example.be.concert.service;

import com.example.be.common.exception.BusinessException;
import com.example.be.concert.domain.Concert;
import com.example.be.concert.dto.ConcertCursor;
import com.example.be.concert.dto.ConcertDetailResponse;
import com.example.be.concert.dto.ConcertSearchCondition;
import com.example.be.concert.dto.ConcertSummaryDto;
import com.example.be.concert.dto.CursorPageResponse;
import com.example.be.concert.enums.ConcertSortType;
import com.example.be.concert.exception.ConcertErrorCode;
import com.example.be.concert.repository.ConcertRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {

  private final ConcertRepository concertRepository;

  @Transactional
  public ConcertDetailResponse getDetailPageData(Long concertId) {
    Concert concert =
        concertRepository
            .findByIdWithDetail(concertId)
            .orElseThrow(() -> new BusinessException(ConcertErrorCode.CONCERT_NOT_FOUND));
    concert.incrementViewCount(); // 조회수 상승
    return ConcertDetailResponse.from(concert);
  }

  public CursorPageResponse<ConcertSummaryDto> getConcertListData(
      ConcertSearchCondition condition,
      ConcertCursor cursor,
      int size,
      ConcertSortType sortType,
      boolean isAsc) {
    List<Concert> concerts =
        concertRepository.findWithCursor(condition, cursor, size, sortType, isAsc);
    boolean hasNext = concerts.size() > size;
    if (hasNext) {
      concerts = concerts.subList(0, size);
    }

    String nextCursor = null;
    if (hasNext && !concerts.isEmpty()) {
      Concert last = concerts.get(concerts.size() - 1);
      nextCursor = ConcertCursor.encode(sortType, getSortValue(last, sortType), last.getId());
    }

    List<ConcertSummaryDto> content = concerts.stream().map(ConcertSummaryDto::from).toList();

    return CursorPageResponse.of(content, nextCursor, hasNext, size);
  }

  private Object getSortValue(Concert concert, ConcertSortType sortType) {
    return switch (sortType) {
      case VIEW_COUNT -> concert.getViewCount();
      case PRICE -> concert.getMinPrice();
      case TICKET_OPEN_AT -> concert.getBookingStartAt();
      case CONCERT_DATE -> concert.getStartDate();
    };
  }
}
