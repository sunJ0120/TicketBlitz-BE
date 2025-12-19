package com.example.be.concert;

import com.example.be.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concerts")
@EntityListeners(AuditingEntityListener.class)
public class Concert extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String artist;
  private String description;

  @Column(name = "poster_url")
  private String posterUrl;

  @Embedded
  private Venue venue;

  @Column(nullable = false)
  private LocalDateTime start_date;

  @Column(nullable = false)
  private LocalDateTime end_date;

  @Column(nullable = false)
  private LocalDateTime booking_start_at;

  @Column(nullable = false)
  private LocalDateTime booking_end_at;

  @Enumerated(EnumType.STRING)
  private Status status;
}
