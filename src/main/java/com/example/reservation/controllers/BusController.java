package com.example.reservation.controllers;

import com.example.reservation.entities.Bus;
import com.example.reservation.models.ResponseModel;
import com.example.reservation.services.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bus")
public class BusController {
    @Autowired
    private BusService busService;

    @PostMapping("/add")
    public ResponseModel<Bus> addBus(@RequestBody Bus bus) {
        Bus newBus = busService.addBus(bus);
        return new ResponseModel<>(HttpStatus.CREATED.value(), "Bus saved successfully", newBus);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }
}
