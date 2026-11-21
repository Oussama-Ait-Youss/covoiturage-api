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

import java.math.BigDecimal;
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
        // here i don't consider two passager reserve one last seat, i don't use the lock that declare in the trajte repository
        //Trajet trajet = trajetRepository.findById(request.trajetId()).orElseThrow(() -> new TrajetNotFoundException("trajet not found..."));

        Trajet trajet = trajetRepository.findByIdForUpdate(
                request.trajetId()
        ).orElseThrow(() ->
                new TrajetNotFoundException(
                        "Trip not found with id: " + request.trajetId()
                )
        );



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

        // calculate the montantTotal
        // 1. Calculate segment price
        BigDecimal prixSegment = pointDescente.getPrixDepuisDepart()
                .subtract(pointMontee.getPrixDepuisDepart());

        // 2. Calculate total amount
        BigDecimal montantTotal = prixSegment.multiply(
                BigDecimal.valueOf(request.nombrePlaces())
        );


        //create reservation
        Reservation reservation = reservationMapper.toEntity(request);

        // Assign relationships
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setPointMontee(pointMontee);
        reservation.setPointDescente(pointDescente);

        // Assign calculated fields
        reservation.setMontantTotal(montantTotal);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saveReservation = reservationRepository.save(reservation);

        //update the trajet status

        long remainingAfterBooking =
                remainingSeats - request.nombrePlaces();

        if (remainingAfterBooking == 0) {
            trajet.setStatus(StatutTrajet.COMPLET);
            trajetRepository.save(trajet);
        }


        return reservationMapper.toResponse(saveReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long id) {

        // 1. Find reservation
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found with id: " + id
                        )
                );

        // 2. Convert entity to DTO
        return reservationMapper.toResponse(reservation);
    }



    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByPassager(
            Long passagerId) {

        // 1. Verify that the passenger exists
        passagerRepository.findById(passagerId)
                .orElseThrow(() ->
                        new PassagerNotFoundException(
                                "Passenger not found with id: " + passagerId
                        )
                );

        // 2. Get reservations
        List<Reservation> reservations =
                reservationRepository.findByPassagerId(passagerId);

        // 3. Convert each reservation to a response
        return reservations.stream()
                .map(reservationMapper::toResponse)
                .toList();
    }



    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByTrajet(
            Long trajetId) {

        // 1. Verify that the trip exists
        trajetRepository.findById(trajetId)
                .orElseThrow(() ->
                        new TrajetNotFoundException(
                                "Trip not found with id: " + trajetId
                        )
                );

        // 2. Get reservations for this trip
        List<Reservation> reservations =
                reservationRepository.findByTrajetId(trajetId);

        // 3. Convert to DTOs
        return reservations.stream()
                .map(reservationMapper::toResponse)
                .toList();
    }




    @Override
    @Transactional
    public void cancelReservation(Long id) {

        // 1. Find reservation
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found with id: " + id
                        )
                );

        // 2. Lock the trip
        Trajet trajet = trajetRepository.findByIdForUpdate(
                reservation.getTrajet().getId()
        ).orElseThrow(() ->
                new TrajetNotFoundException("Trip not found")
        );

        // 3. Check reservation status
        if (reservation.getStatut() != StatutReservation.CONFIRMEE) {
            throw new ReservationCancellationException(
                    "Reservation is already cancelled"
            );
        }

        // 4. Check trip status
        if (trajet.getStatus() == StatutTrajet.TERMINE
                || trajet.getStatus() == StatutTrajet.ANNULE) {

            throw new ReservationCancellationException(
                    "Cannot cancel a reservation for this trip"
            );
        }

        // 5. Check 24-hour cancellation deadline
        LocalDateTime limiteAnnulation =
                trajet.getDateDepart().minusHours(24);

        if (LocalDateTime.now().isAfter(limiteAnnulation)) {
            throw new ReservationCancellationException(
                    "Cancellation must be at least 24 hours before departure"
            );
        }

        // 6. Cancel reservation
        reservation.setStatut(StatutReservation.ANNULEE);

        reservationRepository.save(reservation);

        // 7. Recalculate confirmed reserved seats
        Long seatsReserved = reservationRepository.sumPlacesReservees(
                trajet.getId(),
                StatutReservation.CONFIRMEE
        );

        long remainingSeats =
                trajet.getPlacesProposees() - seatsReserved;

        // 8. Reopen the trip if it was full
        if (trajet.getStatus() == StatutTrajet.COMPLET
                && remainingSeats > 0) {

            trajet.setStatus(StatutTrajet.PUBLIE);
            trajetRepository.save(trajet);
        }
    }





}
