package com.example.reservation.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    String healthy() {
        return "Bus API is healthy.";
    }
}
