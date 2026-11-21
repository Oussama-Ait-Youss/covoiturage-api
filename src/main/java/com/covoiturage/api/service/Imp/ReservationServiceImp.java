package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.reservation.ReservationCreateRequest;
import com.covoiturage.api.dto.reservation.ReservationResponse;
import com.covoiturage.api.entity.Passager;
import com.covoiturage.api.entity.PointArret;
import com.covoiturage.api.entity.Reservation;
import com.covoiturage.api.entity.Trajet;
import com.covoiturage.api.entity.enums.StatutReservation;
import com.covoiturage.api.entity.enums.StatutTrajet;
import com.covoiturage.api.exception.*;
import com.covoiturage.api.mapper.ReservationMapper;
import com.covoiturage.api.repository.PassagerRepository;
import com.covoiturage.api.repository.PointArretRepository;
import com.covoiturage.api.repository.ReservationRepository;
import com.covoiturage.api.repository.TrajetRepository;
import com.covoiturage.api.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReservationServiceImp implements ReservationService {


    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final PassagerRepository passagerRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImp(
            ReservationRepository reservationRepository,
            TrajetRepository trajetRepository,
            PassagerRepository passagerRepository,
            PointArretRepository pointArretRepository,
            ReservationMapper reservationMapper) {

        this.reservationRepository = reservationRepository;
        this.trajetRepository = trajetRepository;
        this.passagerRepository = passagerRepository;
        this.pointArretRepository = pointArretRepository;
        this.reservationMapper = reservationMapper;
    }




    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request){

        //get the passager
        Passager passager = passagerRepository.findById(request.passagerId()).orElseThrow(() -> new PassagerNotFoundException("passager not found"));


        //check if the passager not active
        if (!Boolean.TRUE.equals(passager.isActif())) {
            throw new UserNotActiveException(
                    "The passenger account is inactive"
            );
        }


        //get the trajet
        Trajet trajet = trajetRepository.findById(request.trajetId()).orElseThrow(() -> new TrajetNotFoundException("trajet not found..."));


        //validate the trajet status
        if (trajet.getStatus() != StatutTrajet.PUBLIE) {
            throw new TrajetNotOpenException(
                    "The trip is not available for reservation"
            );
        }

        //find the the stoping points
        PointArret pointMontee = pointArretRepository.findById(request.pointMonteeId()).orElseThrow(() -> new PointArretNotFound("point arret not found"));
        PointArret pointDescente = pointArretRepository.findById(request.pointDescenteId()).orElseThrow(() -> new PointArretNotFound("point arret not found"));


        //validate that the points belong the trajet.

        if (!pointMontee.getTrajet().getId().equals(trajet.getId())
                || !pointDescente.getTrajet().getId().equals(trajet.getId())) {

            throw new PointArretNotBelongToTrajetException(
                    "Both stops must belong to the selected trip"
            );
        }

        //
        if (!pointMontee.getHeurePassage().isAfter(LocalDateTime.now())) {
            throw new InvalidReservationException(
                    "The boarding time has already passed"
            );
        }


        //validate the order
        if(pointMontee.getOrdre() >= pointDescente.getOrdre()){
            throw new PointArretOrderException("the order of the points arrets is invalide");
        }



        // Validate the requested number of seats
        if (request.nombrePlaces() == null || request.nombrePlaces() < 1) {
            throw new NomberplacesNotValideException(
                    "The number of seats must be at least 1"
            );
        }

        // Get confirmed reserved seats for THIS trip only
        Long seatsReserved = reservationRepository.sumPlacesReservees(
                trajet.getId(),
                StatutReservation.CONFIRMEE
        );

        // Calculate remaining seats
        long remainingSeats = trajet.getPlacesProposees() - seatsReserved;

        // Check availability
        if (request.nombrePlaces() > remainingSeats) {
            throw new NumberOfSeatNotEnoughException(
                    "Not enough seats available for this trip"
            );
        }








    }




}
