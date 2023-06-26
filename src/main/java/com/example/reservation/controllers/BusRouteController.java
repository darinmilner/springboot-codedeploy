package com.example.reservation.controllers;

import com.example.reservation.entities.BusRoute;
import com.example.reservation.models.ResponseModel;
import com.example.reservation.services.BusRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class BusRouteController {
    @Autowired
    private BusRouteService busRouteService;

    @PostMapping("/add")
    public ResponseModel<BusRoute> addRoute(@RequestBody BusRoute busRoute) {
        final BusRoute savedBusRoute = busRouteService.addRoute(busRoute);
        return new ResponseModel<>(HttpStatus.CREATED.value(), "Bus route successfully saved", savedBusRoute);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BusRoute>> getAllRoutes() {
        return ResponseEntity.ok(busRouteService.getAllBusRoutes());
    }

    @GetMapping("/{routeName}")
    public ResponseEntity<BusRoute> getRouteByRouteName(@PathVariable(name = "routeName") String routeName) {
        return ResponseEntity.ok(busRouteService.getRouteByRouteName(routeName));
    }

    @GetMapping("/query")
    public ResponseEntity<BusRoute> getRouteByCityFromAndCityTo(
            @RequestParam String cityFrom,
            @RequestParam String cityTo
    ) {
        return ResponseEntity.ok(busRouteService.getRouteByCityFromAndCityTo(cityFrom, cityTo));
    }
}
