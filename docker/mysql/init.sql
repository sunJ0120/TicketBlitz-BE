-- ================================================
-- TICKETBLITZ - MySQL
-- ================================================

-- ------------------------------------------------
-- 1. 회원 (users)
-- ------------------------------------------------
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NULL,
    name       VARCHAR(100)        NOT NULL,
    role       VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 1-1. 소셜 계정 연동 (social_accounts)
-- ------------------------------------------------
CREATE TABLE social_accounts
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    provider    VARCHAR(20)  NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_social_account_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uk_provider_account UNIQUE (provider, provider_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 2. 건물 (buildings)
-- ------------------------------------------------
CREATE TABLE buildings
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    address    VARCHAR(300) NOT NULL,
    latitude   DOUBLE       NOT NULL,
    longitude  DOUBLE       NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 3. 홀 템플릿 (hall_templates)
-- ------------------------------------------------
CREATE TABLE hall_templates
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    building_id BIGINT       NOT NULL,
    hall_name   VARCHAR(100) NOT NULL,
    total_seats INT UNSIGNED NOT NULL,
    total_rows  INT UNSIGNED NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_hall_template_building
        FOREIGN KEY (building_id) REFERENCES buildings (id) ON DELETE CASCADE,
    CONSTRAINT uk_building_hall UNIQUE (building_id, hall_name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 4. 홀 좌석 위치 (hall_seat_positions)
-- ------------------------------------------------
CREATE TABLE hall_seat_positions
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    hall_template_id BIGINT       NOT NULL,
    row_num          INT UNSIGNED NOT NULL,
    seat_num         INT UNSIGNED NOT NULL,
    x_coord          DOUBLE       NOT NULL,
    y_coord          DOUBLE       NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_hall_seat_position_hall
        FOREIGN KEY (hall_template_id) REFERENCES hall_templates (id) ON DELETE CASCADE,
    CONSTRAINT uk_hall_position UNIQUE (hall_template_id, row_num, seat_num)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 5. 공연 (concerts)
-- ------------------------------------------------
CREATE TABLE concerts
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    hall_template_id BIGINT          NOT NULL,
    title            VARCHAR(300)    NOT NULL,
    artist           VARCHAR(200),
    description      TEXT,
    poster_url       VARCHAR(500),

    start_date       TIMESTAMP       NOT NULL,
    end_date         TIMESTAMP       NOT NULL,
    booking_start_at TIMESTAMP       NOT NULL,
    booking_end_at   TIMESTAMP       NOT NULL,
    concert_status   VARCHAR(20)     NOT NULL DEFAULT 'SCHEDULED',
    genre            VARCHAR(20)     NOT NULL,
    view_count       BIGINT UNSIGNED NOT NULL DEFAULT 0,
    min_price        INT UNSIGNED    NOT NULL,
    max_price        INT UNSIGNED    NOT NULL,

    created_at       TIMESTAMP                DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_concert_hall_template
        FOREIGN KEY (hall_template_id) REFERENCES hall_templates (id) ON DELETE RESTRICT,

    INDEX idx_concert_status (concert_status),
    INDEX idx_concert_genre (genre),
    INDEX idx_concert_start_date (start_date),
    INDEX idx_concert_booking_start (booking_start_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 6. 공연별 구역 (concert_sections)
-- ------------------------------------------------
CREATE TABLE concert_sections
(
    concert_id   BIGINT       NOT NULL,
    section_name VARCHAR(50)  NOT NULL,
    row_start    INT UNSIGNED NOT NULL,
    row_end      INT UNSIGNED NOT NULL,
    price        INT UNSIGNED NOT NULL,
    color        VARCHAR(7) DEFAULT '#808080',

    PRIMARY KEY (concert_id, section_name),

    CONSTRAINT fk_concert_section_concert
        FOREIGN KEY (concert_id) REFERENCES concerts (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 7. 공연별 좌석 (concert_seats)
-- ------------------------------------------------
CREATE TABLE concert_seats
(
    id                    BIGINT PRIMARY KEY AUTO_INCREMENT,
    concert_id            BIGINT      NOT NULL,
    hall_seat_position_id BIGINT      NOT NULL,
    section_name          VARCHAR(50) NOT NULL,
    seat_status           VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at            TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_concert_seat_concert
        FOREIGN KEY (concert_id) REFERENCES concerts (id) ON DELETE CASCADE,
    CONSTRAINT fk_concert_seat_position
        FOREIGN KEY (hall_seat_position_id) REFERENCES hall_seat_positions (id),
    CONSTRAINT uk_concert_seat UNIQUE (concert_id, hall_seat_position_id),

    INDEX idx_seat_status (seat_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 8. 예매 (reservations)
-- ------------------------------------------------
CREATE TABLE reservations
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id            BIGINT       NOT NULL,
    seat_id            BIGINT       NOT NULL,
    price              INT UNSIGNED NOT NULL,
    reservation_status VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    reserved_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_at       TIMESTAMP    NULL,
    cancelled_at       TIMESTAMP    NULL,
    expires_at         TIMESTAMP    NULL,
    created_at         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservation_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_reservation_seat
        FOREIGN KEY (seat_id) REFERENCES concert_seats (id),

    INDEX idx_reservation_user (user_id),
    INDEX idx_reservation_status (reservation_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 9. 결제 (payments)
-- ------------------------------------------------
CREATE TABLE payments
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_id    BIGINT       NOT NULL,
    order_id          VARCHAR(50)  NOT NULL UNIQUE,

    amount            INT UNSIGNED NOT NULL,
    payment_method    VARCHAR(20)  NOT NULL,
    payment_status    VARCHAR(20)  NOT NULL DEFAULT 'PENDING',

    pg_transaction_id VARCHAR(100),
    pg_response_code  VARCHAR(20),

    initiated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_at      TIMESTAMP    NULL,
    refunded_at       TIMESTAMP    NULL,
    created_at        TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_reservation
        FOREIGN KEY (reservation_id) REFERENCES reservations (id) ON DELETE RESTRICT,

    INDEX idx_payment_status (payment_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------
-- 10. 결제 로그 (payment_logs)
-- ------------------------------------------------
CREATE TABLE payment_logs
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_id          BIGINT      NOT NULL,
    event_type          VARCHAR(20) NOT NULL,
    old_status          VARCHAR(20),
    new_status          VARCHAR(20),

    pg_response_code    VARCHAR(20),
    pg_response_message TEXT,
    pg_raw_response     TEXT,

    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_log_payment
        FOREIGN KEY (payment_id) REFERENCES payments (id) ON DELETE CASCADE,

    INDEX idx_payment_log_payment (payment_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;