package com.example.be.concert.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Venue {

  @NotBlank(message = "공연장 이름은 필수입니다")
  @Column(name = "venue_name", nullable = false, length = 100)
  private String name;

  @NotBlank(message = "공연장 주소는 필수입니다")
  @Column(name = "venue_address", nullable = false, length = 300)
  private String address;

  @Column(name = "venue_seats")
  private Integer totalSeats;

  @Column(name = "venue_latitude")
  private Double latitude;

  @Column(name = "venue_longitude")
  private Double longitude;

  @Builder
  public Venue(String name, String address,
      Integer totalSeats, Double latitude, Double longitude) {
    this.name = name;
    this.address = address;
    this.totalSeats = totalSeats;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
