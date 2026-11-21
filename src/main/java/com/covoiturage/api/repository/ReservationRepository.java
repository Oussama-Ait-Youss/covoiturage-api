
package com.covoiturage.api.repository;

import com.covoiturage.api.entity.Reservation;
import com.covoiturage.api.entity.enums.StatutReservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT COALESCE(SUM(r.nombrePlaces), 0)
        FROM Reservation r
        WHERE r.trajet.id = :trajetId
          AND r.statut = :statut
    """)
    Long sumPlacesReservees(
            @Param("trajetId") Long trajetId,
            @Param("statut") StatutReservation statut
    );

    boolean existsByTrajetId(Long trajetId);

    List<Reservation> findByTrajetIdAndStatut(
            Long trajetId,
            StatutReservation statut
    );

    // Get reservations for a passenger
    List<Reservation> findByPassagerId(Long passagerId);


    // Get reservations for a trip
    List<Reservation> findByTrajetId(Long trajetId);

    //check if the passager already has a reservation in a trajet
    boolean existsByPassagerIdAndTrajetIdAndStatut(
            Long passagerId,
            Long trajetId,
            StatutReservation statut
    );

}
