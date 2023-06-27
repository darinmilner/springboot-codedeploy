package com.example.reservation.services.impl;

import com.example.reservation.entities.BusRoute;
import com.example.reservation.entities.BusSchedule;
import com.example.reservation.models.ReservationAPIException;
import com.example.reservation.repositories.BusRouteRepository;
import com.example.reservation.repositories.BusScheduleRepository;
import com.example.reservation.services.BusScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusScheduleServiceImpl implements BusScheduleService {
    @Autowired
    private BusScheduleRepository busScheduleRepository;

    @Autowired
    private BusRouteRepository busRouteRepository;

    @Override
    public BusSchedule addSchedule(BusSchedule busSchedule) throws ReservationAPIException {
        final boolean exists = busScheduleRepository.existsByBusAndBusRouteAndDepartureTime(
                busSchedule.getBus(),
                busSchedule.getBusRoute(),
                busSchedule.getDepartureTime()
        );

        if (exists) {
            throw new ReservationAPIException("Schedule already exists.", HttpStatus.CONFLICT);
        }

        return busScheduleRepository.save(busSchedule);
    }

    @Override
    public List<BusSchedule> getAllBusSchedules() {
        return busScheduleRepository.findAll();
    }

    @Override
    public List<BusSchedule> getSchedulesByRoute(String routeName) {
        final BusRoute busRoute = busRouteRepository.findByRouteName(routeName).orElseThrow(
                () -> new ReservationAPIException("Route does not exist", HttpStatus.NOT_FOUND)
        );


        return busScheduleRepository.findByBusRoute(busRoute).orElseThrow(
                () -> new ReservationAPIException("Route does not exist", HttpStatus.NOT_FOUND)
        );
    }
}
