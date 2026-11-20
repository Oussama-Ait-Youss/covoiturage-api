package com.covoiturage.api.service;


import com.covoiturage.api.dto.reservation.ReservationCreateRequest;
import com.covoiturage.api.dto.reservation.ReservationResponse;

import java.util.List;

public interface ReservationService {

    ReservationResponse createReservation(ReservationCreateRequest request);


//
//    ReservationResponse getReservationById(Long id);
//
//
//
//    List<ReservationResponse> getReservationsByPassager(Long passagerId);
//
//
//
//    List<ReservationResponse> getReservationsByTrajet(Long trajetId);
//
//
//
//    void cancelReservation(Long id);


}
