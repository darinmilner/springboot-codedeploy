package com.example.reservation.repositories;

import com.example.reservation.entities.Bus;
import com.example.reservation.entities.BusRoute;
import com.example.reservation.entities.BusSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusScheduleRepository extends JpaRepository<BusSchedule, Long> {
    Optional<List<BusSchedule>> findByBusRoute(BusRoute busRoute);

    Boolean existsByBusAndBusRouteAndDepartureTime(Bus bus, BusRoute busRoute, String date);
}
