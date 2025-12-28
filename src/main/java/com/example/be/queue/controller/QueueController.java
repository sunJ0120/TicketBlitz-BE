package com.example.be.queue.controller;

import com.example.be.queue.dto.QueueStatusResponse;
import com.example.be.queue.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Queue", description = "대기큐 API")
@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

  private final QueueService queueService;

  @Operation(summary = "대기열 입장", description = "대기열 입장 요청")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "대기열 입장 성공")})
  @PostMapping("/{concertId}/enter")
  @SecurityRequirement(name = "BearerAuth")
  public ResponseEntity<QueueStatusResponse> detail(@PathVariable Long concertId) {
    Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return ResponseEntity.ok(queueService.enter(concertId, userId));
  }

  @Operation(summary = "대기열 상태 조회", description = "대기열 상태 요청")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "대기열 상태 조회 성공")})
  @GetMapping("/{concertId}/status")
  @SecurityRequirement(name = "BearerAuth")
  public ResponseEntity<QueueStatusResponse> status(@PathVariable Long concertId) {
    Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return ResponseEntity.ok(queueService.getStatus(concertId, userId));
  }
}
