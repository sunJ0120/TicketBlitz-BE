package com.example.be.concert.dto;

import java.util.List;

public record MainPageResponse(
    long openCount,
    List<ConcertSummaryDto> featuredConcerts
) {

}
