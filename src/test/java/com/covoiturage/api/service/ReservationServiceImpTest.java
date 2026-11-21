package com.covoiturage.api.service;

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
import com.covoiturage.api.service.Imp.ReservationServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImpTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private PassagerRepository passagerRepository;

    @Mock
    private PointArretRepository pointArretRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImp reservationService;

    private Passager passager;
    private Trajet trajet;
    private PointArret pointMontee;
    private PointArret pointDescente;
    private ReservationCreateRequest request;

    @BeforeEach
    void setUp() {
        passager = new Passager();
        passager.setId(1L);
        passager.setActif(true);

        trajet = new Trajet();
        trajet.setId(10L);
        trajet.setPlacesProposees(3);
        trajet.setDateDepart(LocalDateTime.now().plusDays(3));
        trajet.setStatus(StatutTrajet.PUBLIE);

        pointMontee = new PointArret();
        pointMontee.setId(20L);
        pointMontee.setOrdre(1);
        pointMontee.setHeurePassage(LocalDateTime.now().plusDays(1));
        pointMontee.setPrixDepuisDepart(new BigDecimal("40.00"));
        pointMontee.setTrajet(trajet);

        pointDescente = new PointArret();
        pointDescente.setId(21L);
        pointDescente.setOrdre(2);
        pointDescente.setHeurePassage(LocalDateTime.now().plusDays(2));
        pointDescente.setPrixDepuisDepart(new BigDecimal("120.00"));
        pointDescente.setTrajet(trajet);

        request = new ReservationCreateRequest(10L, 1L, 2, 20L, 21L);
    }

    @Test
    void shouldCreateConfirmedReservationWithCorrectAmount() {
        Reservation reservation = new Reservation();
        ReservationResponse response = new ReservationResponse(
                30L, 10L, 1L, 2, 20L, 21L,
                new BigDecimal("160.00"), LocalDateTime.now(),
                StatutReservation.CONFIRMEE
        );

        when(passagerRepository.findById(1L)).thenReturn(Optional.of(passager));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(
                1L, 10L, StatutReservation.CONFIRMEE)).thenReturn(false);
        when(pointArretRepository.findById(20L)).thenReturn(Optional.of(pointMontee));
        when(pointArretRepository.findById(21L)).thenReturn(Optional.of(pointDescente));
        when(reservationRepository.sumPlacesReservees(10L, StatutReservation.CONFIRMEE)).thenReturn(1L);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toResponse(reservation)).thenReturn(response);

        ReservationResponse result = reservationService.createReservation(request);

        assertThat(result).isNotNull();
        assertThat(result.statut()).isEqualTo(StatutReservation.CONFIRMEE);
        assertThat(result.montantTotal()).isEqualByComparingTo("160.00");
        assertThat(reservation.getMontantTotal()).isEqualByComparingTo("160.00");
        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldThrowExceptionWhenPassengerNotFound() {
        when(passagerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(PassagerNotFoundException.class);
    }

    @Test
    void shouldRefuseReservationWhenTripIsNotPublished() {
        trajet.setStatus(StatutTrajet.COMPLET);
        when(passagerRepository.findById(1L)).thenReturn(Optional.of(passager));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(TrajetNotOpenException.class);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientSeatsAvailable() {
        ReservationCreateRequest seatsRequest =
                new ReservationCreateRequest(10L, 1L, 4, 20L, 21L);

        when(passagerRepository.findById(1L)).thenReturn(Optional.of(passager));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(
                1L, 10L, StatutReservation.CONFIRMEE)).thenReturn(false);
        when(pointArretRepository.findById(20L)).thenReturn(Optional.of(pointMontee));
        when(pointArretRepository.findById(21L)).thenReturn(Optional.of(pointDescente));
        when(reservationRepository.sumPlacesReservees(10L, StatutReservation.CONFIRMEE)).thenReturn(0L);

        assertThatThrownBy(() -> reservationService.createReservation(seatsRequest))
                .isInstanceOf(NumberOfSeatNotEnoughException.class);
    }

    @Test
    void shouldRefuseInvalidBoardingAndDropOffOrder() {
        pointMontee.setOrdre(2);
        pointDescente.setOrdre(1);

        when(passagerRepository.findById(1L)).thenReturn(Optional.of(passager));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(
                1L, 10L, StatutReservation.CONFIRMEE)).thenReturn(false);
        when(pointArretRepository.findById(20L)).thenReturn(Optional.of(pointMontee));
        when(pointArretRepository.findById(21L)).thenReturn(Optional.of(pointDescente));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(PointArretOrderException.class);
    }

    @Test
    void shouldRefuseDuplicateConfirmedReservation() {
        when(passagerRepository.findById(1L)).thenReturn(Optional.of(passager));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(
                1L, 10L, StatutReservation.CONFIRMEE)).thenReturn(true);

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @Test
    void shouldCancelConfirmedReservationMoreThan24HoursBeforeDeparture() {
        Reservation reservation = new Reservation();
        reservation.setId(30L);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setTrajet(trajet);

        when(reservationRepository.findById(30L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationRepository.sumPlacesReservees(10L, StatutReservation.CONFIRMEE)).thenReturn(1L);

        reservationService.cancelReservation(30L);

        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.ANNULEE);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldRefuseCancellationWhenReservationIsAlreadyCancelled() {
        Reservation reservation = new Reservation();
        reservation.setId(30L);
        reservation.setStatut(StatutReservation.ANNULEE);
        reservation.setTrajet(trajet);

        when(reservationRepository.findById(30L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));

        assertThatThrownBy(() -> reservationService.cancelReservation(30L))
                .isInstanceOf(ReservationCancellationException.class);
    }

    @Test
    void shouldRefuseCancellationWithin24HoursBeforeDeparture() {
        Reservation reservation = new Reservation();
        reservation.setId(30L);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setTrajet(trajet);
        trajet.setDateDepart(LocalDateTime.now().plusHours(2));

        when(reservationRepository.findById(30L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));

        assertThatThrownBy(() -> reservationService.cancelReservation(30L))
                .isInstanceOf(ReservationCancellationException.class);
    }

    @Test
    void shouldReopenFullTripAfterCancellationFreesASeat() {
        Reservation reservation = new Reservation();
        reservation.setId(30L);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setTrajet(trajet);
        trajet.setStatus(StatutTrajet.COMPLET);

        when(reservationRepository.findById(30L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationRepository.sumPlacesReservees(10L, StatutReservation.CONFIRMEE)).thenReturn(2L);

        reservationService.cancelReservation(30L);

        assertThat(trajet.getStatus()).isEqualTo(StatutTrajet.PUBLIE);
        verify(trajetRepository).save(trajet);
    }
}
