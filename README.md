# 🎫 TICKETBLITZ

> 대용량 트래픽 처리를 위한 고성능 콘서트 티켓팅 시스템

<img src="ticketblitz_icon.png" width=70/>

## 📋 프로젝트 개요

TICKETBLITZ는 인기 공연의 오픈런 상황에서 발생하는 대량의 동시 접속을 안정적으로 처리하기 위한 티켓 예매 시스템입니다.

### 🎯 핵심 목표

- 동시 접속자 10만 명 이상 처리
- 좌석 선점 시 동시성 제어
- 대기열 시스템을 통한 트래픽 제어
- 안정적인 결제 처리 및 추적

---

## 🔥 핵심 기능 요구사항

### 1. 회원 관리

- [x] 이메일/비밀번호 기반 회원가입 및 로그인
- [x] 소셜 로그인 (카카오, 네이버, 구글)
- [x] JWT 기반 인증/인가
- [x] Redis를 활용한 세션 관리 (블랙 리스트, 화이트 리스트)

### 2. 공연 관리

**공연장 템플릿 시스템**

- [x] 공연장별 좌석 구역 템플릿 생성 (재사용 가능)
- [x] 구역별 행/열 정보 및 색상 관리
- [x] 공연마다 템플릿 참조 및 가격 설정

**공연 정보**

- [x] 공연 기본 정보 (제목, 아티스트, 설명, 포스터)
- [x] Embedded Venue (공연장 정보 내장)
- [x] 공연/예매 시작/종료 일시 관리
- [x] 공연 상태 관리 (예정, 예매중, 매진, 종료 등)

### 3. 좌석 관리

- [x] 공연별 구역 생성 (VIP, R석, S석, A석 등)
- [x] 구역별 좌석 동적 생성 (행/열 기반)
- [x] 좌석 상태 관리 (예매 가능, 선택중, 예약완료, 판매완료)
- [ ] 실시간 좌석 현황 조회 (Redis 캐싱)

### 4. 예매 프로세스

**대기열 시스템**

- [ ] Redis Sorted Set 기반 대기열 구현
- [ ] 실시간 대기 순번 및 예상 대기 시간 제공
- [ ] TTL 기반 토큰 자동 만료
- [ ] 대기열 → 활성 토큰 전환 스케줄러

**좌석 선택 및 예약**

- [ ] 좌석 선점 시 분산 락 (Redis/Redisson)
- [ ] 15분 임시 예약 (만료 시 자동 해제)
- [ ] WebSocket을 통한 실시간 좌석 상태 업데이트
- [x] 예약 상태 관리 (대기, 확정, 취소, 만료)

### 5. 결제 시스템

- [x] PG사 연동 준비 (다날 예정)
- [x] 0원 결제 지원 (포인트 전액 사용, 쿠폰 등)
- [x] 결제 상태 관리 (대기, 완료, 실패, 취소, 환불)
- [x] 결제 이력 추적 (PaymentLog)
- [ ] 결제 실패 시 자동 재시도
- [ ] 환불 처리

### 6. 성능 최적화

- [ ] Redis 캐싱 전략 (공연 정보, 좌석 현황)
- [ ] DB 인덱싱 최적화
- [ ] 조회 쿼리 N+1 해결 (Fetch Join)
- [ ] 대용량 데이터 페이징 처리

---

## 🌟 아키텍처 특징

### 하이브리드 아키텍처

- **Spring MVC (블로킹)**: 트랜잭션이 중요한 쓰기 작업
    - 예매 처리, 결제, 회원가입 등
    - Spring Data JPA 활용

- **Spring WebFlux (논블로킹)**: 대량의 읽기 요청
    - 실시간 좌석 현황 조회
    - 대기열 순번 조회
    - 공연 목록 조회
    - WebClient로 외부 API 호출 (PG사 등)

### Why Hybrid?

- JPA의 편리한 ORM 기능 활용
- 트랜잭션 관리의 단순성 유지
- 대량 조회 요청은 WebFlux로 처리
- 점진적 학습 및 전환 가능

---

## 🛠️ 기술 스택

### Backend - Core

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring MVC](https://img.shields.io/badge/Spring%20MVC-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

### Backend - Reactive

![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![WebClient](https://img.shields.io/badge/WebClient-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

### Security & Auth

![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-EB5424?style=for-the-badge&logo=auth0&logoColor=white)

### Database & Cache

![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-blue?style=for-the-badge)
![Redis](https://img.shields.io/badge/Redis-7.0-DC382D?style=for-the-badge&logo=redis&logoColor=white)

### Monitoring & Logging

![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)
![Micrometer](https://img.shields.io/badge/Micrometer-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

### DevOps & Infrastructure

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker%20Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![AWS ECS](https://img.shields.io/badge/AWS%20ECS-FF9900?style=for-the-badge&logo=amazonecs&logoColor=white)
![AWS ECR](https://img.shields.io/badge/AWS%20ECR-FF9900?style=for-the-badge&logo=amazon&logoColor=white)
![AWS RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)
![AWS ElastiCache](https://img.shields.io/badge/AWS%20ElastiCache-C925D1?style=for-the-badge&logo=amazon&logoColor=white)
![AWS ALB](https://img.shields.io/badge/AWS%20ALB-FF9900?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white)

### Tools

![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
