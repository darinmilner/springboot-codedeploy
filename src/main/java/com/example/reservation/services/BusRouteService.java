package com.example.reservation.services;

import com.example.reservation.entities.BusRoute;

import java.util.List;

public interface BusRouteService {
    BusRoute addRoute(BusRoute busRoute);

    List<BusRoute> getAllBusRoutes();

    BusRoute getRouteByRouteName(String routeName);

    BusRoute getRouteByCityFromAndCityTo(String cityFrom, String cityTo);
}
