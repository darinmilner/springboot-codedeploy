package com.example.reservation.services;

import com.example.reservation.entities.Bus;

import java.util.List;

public interface BusService {
    Bus addBus(Bus bus);

    List<Bus> getAllBuses();
}
