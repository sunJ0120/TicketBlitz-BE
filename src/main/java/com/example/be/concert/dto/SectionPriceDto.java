package com.example.be.concert.dto;

import com.example.be.concert.domain.ConcertSection;

public record SectionPriceDto(String sectionName, int price) {

  public static SectionPriceDto from(ConcertSection section) {
    return new SectionPriceDto(section.getSectionLabel().getDisplayName(), section.getPrice());
  }
}
