
package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.trajet.*;
import com.covoiturage.api.dto.pointarret.PointArretCreateRequest;
import com.covoiturage.api.entity.*;
import com.covoiturage.api.entity.enums.StatutTrajet;
import com.covoiturage.api.entity.enums.StatutReservation;
import com.covoiturage.api.exception.*;
import com.covoiturage.api.mapper.TrajetMapper;
import com.covoiturage.api.repository.*;
import com.covoiturage.api.service.TrajetService;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrajetServiceImp implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ReservationRepository reservationRepository;
    private final TrajetMapper trajetMapper;

    public TrajetServiceImp(
            TrajetRepository trajetRepository,
            VehiculeRepository vehiculeRepository,
            ReservationRepository reservationRepository,
            TrajetMapper trajetMapper) {

        this.trajetRepository = trajetRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.reservationRepository = reservationRepository;
        this.trajetMapper = trajetMapper;
    }

    // ========== CREATE ==========

    @Override
    @Transactional
    public TrajetResponse createTrajet(TrajetCreateRequest request) {

        // 1. Find vehicle
        Vehicule vehicule = vehiculeRepository.findById(
                request.vehiculeId()
        ).orElseThrow(() ->
                new VehiculeNotFoundException("Vehicule not found")
        );

        // 2. Check ownership
        if (!vehicule.getConducteur().getId()
                .equals(request.conducteurId())) {

            throw new InvalidTrajetException(
                    "Vehicle does not belong to this driver"
            );
        }

        // 3. Check driver status
        if (!Boolean.TRUE.equals(
                vehicule.getConducteur().isActif())) {

            throw new UserNotActiveException(
                    "Conducteur is not active"
            );
        }

        // 4. Check proposed seats
        validatePlaces(request.placesProposees(),
                vehicule.getCapacite());

        // 5. Validate and sort stops
        List<PointArretCreateRequest> points =
                validatePoints(request.pointArrets());

        // 6. Create trip
        Trajet trajet = trajetMapper.toEntity(request);

        trajet.setVehicule(vehicule);
        trajet.setStatus(StatutTrajet.PUBLIE);
        trajet.setDateDepart(points.get(0).heurePassage());

        // 7. Create the stop entities
        for (PointArretCreateRequest pointRequest : points) {

            PointArret point = new PointArret();

            point.setVille(pointRequest.ville().trim());
            point.setOrdre(pointRequest.ordre());
            point.setHeurePassage(pointRequest.heurePassage());
            point.setPrixDepuisDepart(
                    pointRequest.prixDepuisDepart()
                            .setScale(2, RoundingMode.HALF_UP)
            );

            point.setTrajet(trajet);
            trajet.getPointArrets().add(point);
        }

        // 8. Save trip and stops
        Trajet savedTrajet = trajetRepository.saveAndFlush(trajet);

        return toResponse(savedTrajet);
    }

    // ========== GET BY ID ==========

    @Override
    @Transactional(readOnly = true)
    public TrajetResponse getTrajetById(Long id) {

        Trajet trajet = findTrajet(id);

        return toResponse(trajet);
    }

    // ========== GET ALL ==========

    @Override
    @Transactional(readOnly = true)
    public List<TrajetResponse> getAllTrajets() {

        return trajetRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ========== GET BY CONDUCTEUR ==========

    @Override
    @Transactional(readOnly = true)
    public List<TrajetResponse> getTrajetsByConducteur(
            Long conducteurId) {

        return trajetRepository
                .findByVehiculeConducteurId(conducteurId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ========== SEARCH ==========

    @Override
    @Transactional(readOnly = true)
    public Page<TrajetResponse> searchTrajets(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            BigDecimal prixMax,
            int page,
            int size) {

        if (villeDepart == null || villeDepart.isBlank()
                || villeArrivee == null || villeArrivee.isBlank()
                || date == null) {
            throw new InvalidTrajetException(
                    "Departure city, arrival city and date are required"
            );
        }

        if (nombrePlaces == null || nombrePlaces < 1) {
            throw new InvalidTrajetException(
                    "Number of seats must be at least 1"
            );
        }

        if (prixMax != null
                && prixMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTrajetException(
                    "Maximum price cannot be negative"
            );
        }

        if (page < 0 || size < 1 || size > 100) {
            throw new InvalidTrajetException(
                    "Invalid pagination parameters"
            );
        }

        LocalDateTime debut = date.atStartOfDay();
        LocalDateTime fin = date.plusDays(1).atStartOfDay();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("dateDepart").ascending()
        );

        return trajetRepository.searchTrajets(
                villeDepart.trim(),
                villeArrivee.trim(),
                debut,
                fin,
                StatutTrajet.PUBLIE,
                nombrePlaces,
                prixMax,
                pageable
        ).map(this::toResponse);
    }

    // ========== UPDATE ==========

    @Override
    @Transactional
    public TrajetResponse updateTrajet(
            Long id,
            TrajetUpdateRequest request) {

        Trajet trajet = findTrajet(id);

        if (trajet.getStatus() != StatutTrajet.PUBLIE) {
            throw new InvalidTrajetException(
                    "Only published trips can be updated"
            );
        }

        // Do not replace stops when reservations exist.
        // Reservations refer to existing PointArret IDs.
        if (reservationRepository.existsByTrajetId(id)) {
            throw new InvalidTrajetException(
                    "Cannot modify a trip with reservations"
            );
        }

        validatePlaces(
                request.placesProposees(),
                trajet.getVehicule().getCapacite()
        );

        List<PointArretCreateRequest> points =
                validatePoints(request.pointArrets());

        trajet.setPlacesProposees(request.placesProposees());
        trajet.setDateDepart(points.get(0).heurePassage());

        // Remove existing stops
        trajet.getPointArrets().clear();

        // Replace with the new itinerary
        for (PointArretCreateRequest requestPoint : points) {

            PointArret point = new PointArret();

            point.setVille(requestPoint.ville().trim());
            point.setOrdre(requestPoint.ordre());
            point.setHeurePassage(requestPoint.heurePassage());
            point.setPrixDepuisDepart(
                    requestPoint.prixDepuisDepart()
                            .setScale(2, RoundingMode.HALF_UP)
            );

            point.setTrajet(trajet);
            trajet.getPointArrets().add(point);
        }

        Trajet updated = trajetRepository.saveAndFlush(trajet);

        return toResponse(updated);
    }

    // ========== CANCEL ==========

    @Override
    @Transactional
    public void cancelTrajet(Long id) {

        //Trajet trajet = findTrajet(id);

        Trajet trajet = trajetRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new TrajetNotFoundException(
                                "Trip not found with id: " + id
                        )
                );


        if (trajet.getStatus() == StatutTrajet.ANNULE
                || trajet.getStatus() == StatutTrajet.TERMINE) {

            throw new InvalidTrajetException(
                    "This trip cannot be cancelled"
            );
        }

        List<Reservation> reservations =
                reservationRepository.findByTrajetIdAndStatut(
                        id,
                        StatutReservation.CONFIRMEE
                );

        for (Reservation reservation : reservations) {
            reservation.setStatut(StatutReservation.ANNULEE);
        }

        trajet.setStatus(StatutTrajet.ANNULE);

        reservationRepository.saveAll(reservations);
        trajetRepository.save(trajet);
    }

    // ========== FINISH ==========

    @Override
    @Transactional
    public TrajetResponse terminerTrajet(Long id) {

        //Trajet trajet = findTrajet(id);

        Trajet trajet = trajetRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new TrajetNotFoundException(
                                "Trip not found with id: " + id
                        )
                );


        if (trajet.getStatus() == StatutTrajet.ANNULE
                || trajet.getStatus() == StatutTrajet.TERMINE) {

            throw new InvalidTrajetException(
                    "This trip cannot be finished"
            );
        }

        if (LocalDateTime.now().isBefore(trajet.getDateDepart())) {
            throw new InvalidTrajetException(
                    "The trip cannot finish before departure"
            );
        }

        trajet.setStatus(StatutTrajet.TERMINE);

        Trajet saved = trajetRepository.save(trajet);

        return toResponse(saved);
    }

    // ========== PRIVATE HELPERS ==========

    private Trajet findTrajet(Long id) {

        return trajetRepository.findById(id)
                .orElseThrow(() ->
                        new TrajetNotFoundException(
                                "Trajet not found"
                        )
                );
    }

    private long calculatePlacesRestantes(Trajet trajet) {

        Long reserved = reservationRepository.sumPlacesReservees(
                trajet.getId(),
                StatutReservation.CONFIRMEE
        );

        return trajet.getPlacesProposees() - reserved;
    }

    private TrajetResponse toResponse(Trajet trajet) {

        return trajetMapper.toResponse(
                trajet,
                calculatePlacesRestantes(trajet)
        );
    }

    private void validatePlaces(
            Integer places,
            Integer capacite) {

        if (places == null || capacite == null
                || places < 1 || places > capacite) {

            throw new InvalidTrajetException(
                    "Places must be between 1 and vehicle capacity"
            );
        }
    }

    private List<PointArretCreateRequest> validatePoints(
            List<PointArretCreateRequest> points) {

        if (points == null || points.size() < 2) {
            throw new InvalidTrajetException(
                    "A trip must have at least two stops"
            );
        }

        if (points.stream().anyMatch(Objects::isNull)) {
            throw new InvalidTrajetException(
                    "Stops cannot be null"
            );
        }

        List<PointArretCreateRequest> sorted = points.stream()
                .sorted(Comparator.comparing(
                        PointArretCreateRequest::ordre
                ))
                .toList();

        Set<String> villes = new HashSet<>();
        LocalDateTime previousTime = null;
        BigDecimal previousPrice = null;

        for (int i = 0; i < sorted.size(); i++) {

            PointArretCreateRequest point = sorted.get(i);

            // Orders: 0, 1, 2, 3...
            if (point.ordre() == null || point.ordre() != i) {
                throw new InvalidTrajetException(
                        "Stop orders must start at 0 and be consecutive"
                );
            }

            if (point.ville() == null || point.ville().isBlank()
                    || point.heurePassage() == null
                    || point.prixDepuisDepart() == null) {

                throw new InvalidTrajetException(
                        "Each stop needs city, time and price"
                );
            }

            String city = point.ville().trim().toLowerCase(Locale.ROOT);

            if (!villes.add(city)) {
                throw new InvalidTrajetException(
                        "A city cannot appear twice in the same trip"
                );
            }

            BigDecimal price = point.prixDepuisDepart();

            if (price.scale() > 2 || price.signum() < 0) {
                throw new InvalidTrajetException(
                        "Price must be non-negative with at most 2 decimals"
                );
            }

            if (i == 0) {

                if (!point.heurePassage().isAfter(LocalDateTime.now())) {
                    throw new InvalidTrajetException(
                            "Trip departure must be in the future"
                    );
                }

                if (price.compareTo(BigDecimal.ZERO) != 0) {
                    throw new InvalidTrajetException(
                            "First stop price must be zero"
                    );
                }

            } else {

                if (!point.heurePassage().isAfter(previousTime)) {
                    throw new InvalidTrajetException(
                            "Stop times must strictly increase"
                    );
                }

                if (price.compareTo(previousPrice) <= 0) {
                    throw new InvalidTrajetException(
                            "Stop prices must strictly increase"
                    );
                }
            }

            previousTime = point.heurePassage();
            previousPrice = price;
        }

        return sorted;
    }
}
