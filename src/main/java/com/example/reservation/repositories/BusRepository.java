package com.example.reservation.repositories;

import com.example.reservation.entities.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
}
