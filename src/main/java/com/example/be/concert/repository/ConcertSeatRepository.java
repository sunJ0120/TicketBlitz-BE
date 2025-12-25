package com.example.be.concert.repository;

import com.example.be.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Integer> {}
