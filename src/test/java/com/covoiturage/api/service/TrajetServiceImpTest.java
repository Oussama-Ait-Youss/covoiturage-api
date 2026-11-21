package com.covoiturage.api.service;

import com.covoiturage.api.dto.pointarret.PointArretCreateRequest;
import com.covoiturage.api.dto.trajet.TrajetCreateRequest;
import com.covoiturage.api.dto.trajet.TrajetResponse;
import com.covoiturage.api.dto.trajet.TrajetUpdateRequest;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Reservation;
import com.covoiturage.api.entity.Trajet;
import com.covoiturage.api.entity.Vehicule;
import com.covoiturage.api.entity.enums.StatutReservation;
import com.covoiturage.api.entity.enums.StatutTrajet;
import com.covoiturage.api.exception.InvalidTrajetException;
import com.covoiturage.api.exception.InvalidTrajetDataException;
import com.covoiturage.api.exception.TrajetNotFoundException;
import com.covoiturage.api.exception.UserNotActiveException;
import com.covoiturage.api.exception.VehiculeNotFoundException;
import com.covoiturage.api.exception.VehiculeNotOwnedException;
import com.covoiturage.api.mapper.TrajetMapper;
import com.covoiturage.api.repository.ReservationRepository;
import com.covoiturage.api.repository.TrajetRepository;
import com.covoiturage.api.repository.VehiculeRepository;
import com.covoiturage.api.service.Imp.TrajetServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrajetServiceImpTest {

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TrajetMapper trajetMapper;

    @InjectMocks
    private TrajetServiceImp trajetService;

    private Conducteur conducteur;
    private Vehicule vehicule;
    private TrajetCreateRequest createRequest;
    private Trajet trajet;

    @BeforeEach
    void setUp() {
        conducteur = new Conducteur();
        conducteur.setId(1L);
        conducteur.setActif(true);

        vehicule = new Vehicule();
        vehicule.setId(10L);
        vehicule.setCapacite(4);
        vehicule.setConducteur(conducteur);

        List<PointArretCreateRequest> points = List.of(
                new PointArretCreateRequest(
                        "Rabat",
                        0,
                        LocalDateTime.now().plusDays(3),
                        BigDecimal.ZERO
                ),
                new PointArretCreateRequest(
                        "Marrakech",
                        1,
                        LocalDateTime.now().plusDays(3).plusHours(4),
                        new BigDecimal("120.00")
                )
        );

        createRequest = new TrajetCreateRequest(1L, 10L, 3, points);

        trajet = new Trajet();
        trajet.setId(20L);
        trajet.setPlacesProposees(3);
        trajet.setDateDepart(LocalDateTime.now().plusDays(3));
        trajet.setStatus(StatutTrajet.PUBLIE);
        trajet.setVehicule(vehicule);
    }

    @Test
    void shouldCreateTrajetSuccessfully() {
        TrajetResponse response = new TrajetResponse(
                20L, 1L, 10L, trajet.getDateDepart(), 3, 3L,
                StatutTrajet.PUBLIE, List.of()
        );

        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));
        when(trajetMapper.toEntity(createRequest)).thenReturn(trajet);
        when(trajetRepository.saveAndFlush(trajet)).thenReturn(trajet);
        when(reservationRepository.sumPlacesReservees(20L, StatutReservation.CONFIRMEE)).thenReturn(0L);
        when(trajetMapper.toResponse(trajet, 3L)).thenReturn(response);

        TrajetResponse result = trajetService.createTrajet(createRequest);

        assertThat(result).isNotNull();
        assertThat(trajet.getStatus()).isEqualTo(StatutTrajet.PUBLIE);
        assertThat(trajet.getPointArrets()).hasSize(2);
        verify(trajetRepository).saveAndFlush(trajet);
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        when(vehiculeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trajetService.createTrajet(createRequest))
                .isInstanceOf(VehiculeNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenVehicleBelongsToAnotherDriver() {
        Conducteur autreConducteur = new Conducteur();
        autreConducteur.setId(2L);
        autreConducteur.setActif(true);
        vehicule.setConducteur(autreConducteur);

        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));

        assertThatThrownBy(() -> trajetService.createTrajet(createRequest))
                .isInstanceOf(VehiculeNotOwnedException.class);
    }

    @Test
    void shouldRejectTripWhenSeatsExceedCapacity() {
        TrajetCreateRequest request = new TrajetCreateRequest(
                1L,
                10L,
                5,
                createRequest.pointArrets()
        );

        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));

        assertThatThrownBy(() -> trajetService.createTrajet(request))
                .isInstanceOf(InvalidTrajetDataException.class);
    }

    @Test
    void shouldRejectTripWithFewerThanTwoStops() {
        TrajetCreateRequest request = new TrajetCreateRequest(
                1L,
                10L,
                3,
                List.of(createRequest.pointArrets().get(0))
        );

        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));

        assertThatThrownBy(() -> trajetService.createTrajet(request))
                .isInstanceOf(InvalidTrajetException.class);
    }

    @Test
    void shouldRejectDuplicateCityOrInvalidStopOrder() {
        List<PointArretCreateRequest> points = List.of(
                new PointArretCreateRequest(
                        "Rabat", 0,
                        LocalDateTime.now().plusDays(3),
                        BigDecimal.ZERO
                ),
                new PointArretCreateRequest(
                        "Rabat", 1,
                        LocalDateTime.now().plusDays(3).plusHours(4),
                        new BigDecimal("120.00")
                )
        );
        TrajetCreateRequest request = new TrajetCreateRequest(1L, 10L, 3, points);

        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));

        assertThatThrownBy(() -> trajetService.createTrajet(request))
                .isInstanceOf(InvalidTrajetException.class);
    }

    @Test
    void shouldThrowExceptionWhenTripNotFound() {
        when(trajetRepository.findById(20L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trajetService.getTrajetById(20L))
                .isInstanceOf(TrajetNotFoundException.class);
    }

    @Test
    void shouldRejectUpdateWhenTripHasReservations() {
        TrajetUpdateRequest request = new TrajetUpdateRequest(
                3,
                createRequest.pointArrets()
        );

        when(trajetRepository.findById(20L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.existsByTrajetId(20L)).thenReturn(true);

        assertThatThrownBy(() -> trajetService.updateTrajet(20L, request))
                .isInstanceOf(InvalidTrajetException.class);
    }

    @Test
    void shouldRejectUpdateOfTerminalTrip() {
        trajet.setStatus(StatutTrajet.TERMINE);
        TrajetUpdateRequest request = new TrajetUpdateRequest(
                3,
                createRequest.pointArrets()
        );

        when(trajetRepository.findById(20L)).thenReturn(Optional.of(trajet));

        assertThatThrownBy(() -> trajetService.updateTrajet(20L, request))
                .isInstanceOf(InvalidTrajetException.class);
    }

    @Test
    void shouldCancelTrajetSuccessfully() {
        when(trajetRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.findByTrajetIdAndStatut(
                20L, StatutReservation.CONFIRMEE
        )).thenReturn(List.of());

        trajetService.cancelTrajet(20L);

        assertThat(trajet.getStatus()).isEqualTo(StatutTrajet.ANNULE);
        verify(trajetRepository).save(trajet);
        verify(reservationRepository).saveAll(List.of());
    }

    @Test
    void shouldCancelConfirmedReservationsWithTrajet() {
        Reservation reservation = new Reservation();
        reservation.setId(30L);
        reservation.setStatut(StatutReservation.CONFIRMEE);

        when(trajetRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(trajet));
        when(reservationRepository.findByTrajetIdAndStatut(
                20L, StatutReservation.CONFIRMEE
        )).thenReturn(List.of(reservation));

        trajetService.cancelTrajet(20L);

        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.ANNULEE);
        assertThat(trajet.getStatus()).isEqualTo(StatutTrajet.ANNULE);
        verify(reservationRepository).saveAll(List.of(reservation));
    }

    @Test
    void shouldRejectCancellationOfAlreadyCancelledTrip() {
        trajet.setStatus(StatutTrajet.ANNULE);
        when(trajetRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(trajet));

        assertThatThrownBy(() -> trajetService.cancelTrajet(20L))
                .isInstanceOf(InvalidTrajetException.class);
    }

    @Test
    void shouldRejectFinishBeforeDeparture() {
        when(trajetRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(trajet));

        assertThatThrownBy(() -> trajetService.terminerTrajet(20L))
                .isInstanceOf(InvalidTrajetException.class);
    }

    @Test
    void shouldFinishPastTripSuccessfully() {
        trajet.setDateDepart(LocalDateTime.now().minusDays(1));
        TrajetResponse response = new TrajetResponse(
                20L, 1L, 10L, trajet.getDateDepart(), 3, 3L,
                StatutTrajet.TERMINE, List.of()
        );

        when(trajetRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(trajet));
        when(trajetRepository.save(trajet)).thenReturn(trajet);
        when(reservationRepository.sumPlacesReservees(20L, StatutReservation.CONFIRMEE)).thenReturn(0L);
        when(trajetMapper.toResponse(trajet, 3L)).thenReturn(response);

        TrajetResponse result = trajetService.terminerTrajet(20L);

        assertThat(trajet.getStatus()).isEqualTo(StatutTrajet.TERMINE);
        assertThat(result.status()).isEqualTo(StatutTrajet.TERMINE);
        verify(trajetRepository).save(trajet);
    }

    @Test
    void shouldRejectInvalidSearchParameters() {
        assertThatThrownBy(() ->
                trajetService.searchTrajets("", "Marrakech", LocalDate.now(), 1, null, 0, 10)
        ).isInstanceOf(InvalidTrajetException.class);
    }

    @Test
    void shouldSearchTripsWithPagination() {
        Page<Trajet> tripPage = new PageImpl<>(List.of(trajet));
        TrajetResponse response = new TrajetResponse(
                20L, 1L, 10L, trajet.getDateDepart(), 3, 3L,
                StatutTrajet.PUBLIE, List.of()
        );

        when(trajetRepository.searchTrajets(
                eq("Rabat"),
                eq("Marrakech"),
                eq(LocalDate.now().atStartOfDay()),
                eq(LocalDate.now().plusDays(1).atStartOfDay()),
                eq(StatutTrajet.PUBLIE),
                eq(2),
                eq(null),
                any(Pageable.class)
        )).thenReturn(tripPage);
        when(reservationRepository.sumPlacesReservees(20L, StatutReservation.CONFIRMEE)).thenReturn(0L);
        when(trajetMapper.toResponse(trajet, 3L)).thenReturn(response);

        Page<TrajetResponse> result =
                trajetService.searchTrajets("Rabat", "Marrakech", LocalDate.now(), 2, null, 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).status()).isEqualTo(StatutTrajet.PUBLIE);
        verify(trajetRepository).searchTrajets(
                eq("Rabat"),
                eq("Marrakech"),
                eq(LocalDate.now().atStartOfDay()),
                eq(LocalDate.now().plusDays(1).atStartOfDay()),
                eq(StatutTrajet.PUBLIE),
                eq(2),
                eq(null),
                any(Pageable.class)
        );
    }
}
