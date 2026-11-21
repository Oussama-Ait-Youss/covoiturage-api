package com.covoiturage.api.controller.reservation;


import com.covoiturage.api.dto.reservation.ReservationCreateRequest;
import com.covoiturage.api.dto.reservation.ReservationResponse;
import com.covoiturage.api.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;


    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(
            @Valid @RequestBody ReservationCreateRequest request){
        return reservationService.createReservation(request);
    }
    // GET RESERVATION BY ID
    @GetMapping("/{id}")
    public ReservationResponse getReservationById(
            @PathVariable Long id) {

        return reservationService.getReservationById(id);
    }

    // GET RESERVATIONS BY PASSENGER
    @GetMapping("/passagers/{passagerId}")
    public List<ReservationResponse> getReservationsByPassager(
            @PathVariable Long passagerId) {

        return reservationService.getReservationsByPassager(
                passagerId
        );
    }

    // GET RESERVATIONS BY TRIP
    @GetMapping("/trajets/{trajetId}")
    public List<ReservationResponse> getReservationsByTrajet(
            @PathVariable Long trajetId) {

        return reservationService.getReservationsByTrajet(
                trajetId
        );
    }

    // CANCEL RESERVATION
    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(
            @PathVariable Long id) {

        reservationService.cancelReservation(id);
    }
}
