package com.example.be.concert.domain;

import com.example.be.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "section_templates",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_venue_section",
            columnNames = {"venue_name", "section_name"}
        )
    }
)
@EntityListeners(AuditingEntityListener.class)
public class SectionTemplate extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "공연장 이름은 필수입니다")
  @Column(name = "venue_name", nullable = false, length = 100)
  private String venueName;

  @NotBlank(message = "섹션 이름은 필수입니다")
  @Column(name = "section_name", nullable = false, length = 50)
  private String sectionName;

  @Min(value = 1, message = "행 수는 1 이상이어야 합니다")
  @Column(name = "row_count", nullable = false)
  private int rowCount;

  @Min(value = 1, message = "열당 좌석 수는 1 이상이어야 합니다")
  @Column(name = "seats_per_row", nullable = false)
  private int seatsPerRow;

  @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "올바른 색상 코드 형식이 아닙니다")
  @Column(name = "color", length = 7)
  private String color = "#808080";    // HEX color code (#FFFFFF)

  @Builder
  public SectionTemplate(String venueName, String sectionName,
      int rowCount, int seatsPerRow, String color) {
    this.venueName = venueName;
    this.sectionName = sectionName;
    this.rowCount = rowCount;
    this.seatsPerRow = seatsPerRow;
    this.color = color != null ? color : "#808080";
  }
}
