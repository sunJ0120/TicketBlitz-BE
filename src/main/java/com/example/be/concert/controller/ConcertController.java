package com.example.be.concert.controller;

import com.example.be.concert.dto.ConcertCursor;
import com.example.be.concert.dto.ConcertDetailResponse;
import com.example.be.concert.dto.ConcertSearchCondition;
import com.example.be.concert.dto.ConcertSummaryDto;
import com.example.be.concert.dto.CursorPageResponse;
import com.example.be.concert.enums.ConcertSortType;
import com.example.be.concert.service.ConcertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Concert", description = "공연 API")
@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

  private final ConcertService concertService;

  @Operation(summary = "상세 페이지", description = "상세 페이지에 필요한 데이터")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "상세 페이지 조회 성공")})
  @GetMapping("/{id}")
  public ResponseEntity<ConcertDetailResponse> detail(@PathVariable Long id) {
    return ResponseEntity.ok(concertService.getDetailPageData(id));
  }

  @Operation(summary = "콘서트 목록 조회", description = "커서 기반 페이지네이션으로 콘서트 목록 조회")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "목록 조회 성공")})
  @GetMapping()
  public ResponseEntity<CursorPageResponse<ConcertSummaryDto>> concertsList(
      @ModelAttribute ConcertSearchCondition condition,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "20") Integer size,
      @RequestParam(defaultValue = "CONCERT_DATE") ConcertSortType sortType,
      @RequestParam(defaultValue = "false") boolean isAsc) {

    ConcertCursor concertCursor = ConcertCursor.decode(cursor);
    return ResponseEntity.ok(
        concertService.getConcertListData(condition, concertCursor, size, sortType, isAsc));
  }
}
