package com.example.reservation.services.impl;

import com.example.reservation.entities.BusRoute;
import com.example.reservation.models.ReservationAPIException;
import com.example.reservation.repositories.BusRouteRepository;
import com.example.reservation.services.BusRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusRouteServiceImpl implements BusRouteService {
    @Autowired
    private BusRouteRepository busRouteRepository;

    @Override
    public BusRoute addRoute(BusRoute busRoute) {
        return busRouteRepository.save(busRoute);
    }

    @Override
    public List<BusRoute> getAllBusRoutes() {
        return busRouteRepository.findAll();
    }

    @Override
    public BusRoute getRouteByRouteName(String routeName) {
        return busRouteRepository.findByRouteName(routeName).orElseThrow(() -> new ReservationAPIException(
                        "No such bus route was found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public BusRoute getRouteByCityFromAndCityTo(String cityFrom, String cityTo) {
        return busRouteRepository.findByCityFromAndCityTo(
                cityFrom,
                cityTo
        ).orElseThrow(() -> new ReservationAPIException(
                        "No such bus route was found",
                        HttpStatus.NOT_FOUND
                )
        );
    }
}
