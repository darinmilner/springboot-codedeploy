package com.example.reservation.services.impl;

import com.example.reservation.entities.BusSchedule;
import com.example.reservation.entities.Customer;
import com.example.reservation.entities.Reservation;
import com.example.reservation.models.ReservationAPIException;
import com.example.reservation.repositories.BusScheduleRepository;
import com.example.reservation.repositories.CustomerRepository;
import com.example.reservation.repositories.ReservationRepository;
import com.example.reservation.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BusScheduleRepository busScheduleRepository;

    @Override
    public Reservation addReservation(Reservation reservation) {
        final Customer customer;
        final boolean doesCustomerExist = customerRepository.existsByMobileOrEmail(
                reservation.getCustomer().getMobile(),
                reservation.getCustomer().getEmail()
        );
        if (doesCustomerExist) {
            customer = customerRepository.findByMobileOrEmail(
                    reservation.getCustomer().getMobile(),
                    reservation.getCustomer().getEmail()
            ).orElseThrow(() -> new ReservationAPIException(
                            "Customer does not exist", HttpStatus.NOT_FOUND
                    )
            );
        } else {
            customer = customerRepository.save(reservation.getCustomer());
        }
        reservation.setCustomer(customer);
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByScheduleAndDepartureDate(Long scheduleId, String departureDate) {
        final BusSchedule schedule = busScheduleRepository.findById(
                scheduleId
        ).orElseThrow(() -> new ReservationAPIException(
                        "Bus Schedule Not Found",
                        HttpStatus.NOT_FOUND
                )
        );
        return reservationRepository.findByBusScheduleAndDepartureDate(
                schedule, departureDate
        ).orElseThrow(() -> new ReservationAPIException(
                        "Reservation Not Found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public List<Reservation> getReservationsByMobile(String mobile) {
        final Customer customer = customerRepository.findByMobile(
                mobile
        ).orElseThrow(() -> new ReservationAPIException(
                        "No customer found",
                        HttpStatus.NOT_FOUND
                )
        );

        return reservationRepository.findByCustomer(customer).orElseThrow(
                () -> new ReservationAPIException(
                        "No customer found",
                        HttpStatus.NOT_FOUND
                )
        );
    }
}
